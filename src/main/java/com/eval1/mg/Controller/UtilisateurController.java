package com.eval1.mg.Controller;

import com.eval1.mg.Exception.ValeurInvalideException;
import com.eval1.mg.Model.Profil;
import com.eval1.mg.Model.Utilisateur;

import com.eval1.mg.Repository.UtilisateurRepository;

import com.eval1.mg.Security.CustomUserDetails;
import com.eval1.mg.Service.ReinitialisationBaseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@Controller
public class UtilisateurController {
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	ReinitialisationBaseService reinitialisationBaseService;

	@GetMapping("/loginMilay")
	public String login(Model model) throws ValeurInvalideException, IOException {
		Utilisateur u = new Utilisateur();
		u.setEmail("toavina@gmail.com");
		u.setPassword("toavina");
		model.addAttribute("utilisateur",u);

		return "Auth/login";
	}

	@GetMapping("/")
	public String loginClient(Model model) throws ValeurInvalideException, IOException {
		Utilisateur u = new Utilisateur();
		u.setContact("0340590098");
		model.addAttribute("utilisateur",u);

		return "Auth/loginClient";
	}

	@PostMapping("/loginClient")
	public String loginClient(Utilisateur u, RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request){

		u.setContact(u.getContact().trim());
		Utilisateur user = utilisateurRepository.findUtilisateurByContact(u.getContact());

		if(user==null){
			user = new Utilisateur();
			user.setProfil(Profil.CLIENT);
			user.setGenre(1);
			user.setContact(u.getContact());
			user = utilisateurRepository.save(user);
		}

		UserDetails userDetails = new CustomUserDetails(user);

		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

		// Définissez le contexte de sécurité avec cette authentification
		SecurityContextHolder.getContext().setAuthentication(authentication);

		HttpSession s = request.getSession(true);
		session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());


		System.out.println(user.getProfil());

		session.setAttribute("user" , user);

		System.out.println(user.getContact());

		// Redirigez vers la page d'accueil ou toute autre page appropriée
		return "redirect:/v1/construction/user";
	}


	@PostMapping("/login")
	public String login(Utilisateur u, RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request){

		Utilisateur user  = utilisateurRepository.findUtilisateurByEmail(u.getEmail());
		if(user==null || !passwordEncoder.matches(u.getPassword(),user.getPassword())){
			redirectAttributes.addFlashAttribute("error" , "Mot de passe ou email non valide");
			return "redirect:/loginMilay";
		}
		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(u.getEmail(), u.getPassword());

		Authentication authentication = authenticationManager.authenticate(authRequest);
		SecurityContext sc = SecurityContextHolder.getContext();
		sc.setAuthentication(authentication);


		HttpSession s = request.getSession(true);
		session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, sc);

		session.setAttribute("user" , user);


		return "redirect:/v1/construction/admin";
	}

	@GetMapping("/v1/accueil")
	public String accueil(){
		return "Accueil/index";
	}

	@GetMapping("/insc")
	public String insc(Model model)  {
		Utilisateur u = new Utilisateur();
		model.addAttribute("utilisateur",u);
		return "Auth/inscription";
	}

	@PostMapping("/inscription")
	public String inscription(@Valid Utilisateur u, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpSession session, HttpServletRequest request){
		if(u.getEmail()!=null){
			Utilisateur user  = utilisateurRepository.findUtilisateurByEmail(u.getEmail());

			if(user != null){
				redirectAttributes.addFlashAttribute("error" , "Email deja prise");
				return "redirect:/insc";
			}
		}

		if(bindingResult.hasErrors()){
			String message = "";
			for (int i = 0; i < bindingResult.getAllErrors().size(); i++) {
				message += bindingResult.getAllErrors().get(i).getDefaultMessage()+";";
			}
			redirectAttributes.addFlashAttribute("error", message);
			System.out.println(message);
			return "redirect:/insc";
		}

		u.setPassword(passwordEncoder.encode(u.getPassword()));

		int number_profil = -1;
		if(u.getProfil().equals(Profil.ADMIN)) {
			number_profil = 1;
		}

		int result = utilisateurRepository.inscription(u.getContact(),u.getDateNaissance(),u.getEmail(),u.getGenre(),u.getNom(),u.getPassword(),u.getPrenom(),number_profil);
		//utilisateurRepository.save(u);
		if(result==0){
			redirectAttributes.addFlashAttribute("error" , "Admin ou client?");
			return "redirect:/";
		}
		redirectAttributes.addFlashAttribute("message" , "Inscription avec succes");
		return "redirect:/";
	}

	@GetMapping("/deco")
	public String logout(HttpSession session,RedirectAttributes redirectAttributes,HttpServletRequest request){
		session.removeAttribute("user");
		HttpSession s = request.getSession(true);
		session.removeAttribute(SPRING_SECURITY_CONTEXT_KEY);
		redirectAttributes.addFlashAttribute("message" ,"Veuillez vous reconnecter");
		return "redirect:/loginClient";
	}

	@GetMapping("/error/403")
	public String error403(){
		return "Auth/403Error";
	}
	@GetMapping("/error/404")
	public String error404(){
		return "Auth/404Error";
	}


	@GetMapping("/reinitialiserBase")
	public String reinitialiserBase(RedirectAttributes redirectAttributes) {


		Utilisateur u =new Utilisateur();
		u.setProfil(Profil.ADMIN);

		try{
			reinitialisationBaseService.desactiverContraintes();
			reinitialisationBaseService.effacerDonneesToutesEntites(u);

			utilisateurRepository.deleteUtilisateurByProfil("CLIENT");

			reinitialisationBaseService.reactiverContraintes();
		}catch (Exception e){
			redirectAttributes.addFlashAttribute("error" ,e.getMessage());
			return "redirect:/loginMilay";
		}
		redirectAttributes.addFlashAttribute("message" ,"Reinitialisation avec succes");
		return "redirect:/loginMilay";
	}


}
