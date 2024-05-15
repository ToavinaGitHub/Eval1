package com.eval1.mg.Service;


import com.eval1.mg.Model.Profil;
import com.eval1.mg.Model.Utilisateur;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceUnit;
import jakarta.persistence.Table;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.hibernate.metamodel.MappingMetamodel;
import org.hibernate.metamodel.model.domain.internal.MappingMetamodelImpl;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import jakarta.persistence.PersistenceContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service
public class ReinitialisationBaseService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    private Map<String, List<String>> contraintesDesactivees = new HashMap<>();

    public String getTableName(Class<?> entityClass){
        Table tableAnnotation = entityClass.getAnnotation(Table.class);
        if (tableAnnotation != null && !tableAnnotation.name().isEmpty()) {
            return tableAnnotation.name();
        } else {
            Metamodel metamodel = entityManager.getMetamodel();
            EntityType<?> entityType = metamodel.entity(entityClass);
            return entityType.getName();
        }
    }

    public List<String> listerContraintes(Class<?> entityClass) {
        String tableName = getTableName(entityClass);
        return entityManager
                .createNativeQuery("SELECT constraint_name FROM information_schema.table_constraints WHERE table_name = :tableName AND constraint_type = 'FOREIGN KEY'")
                .setParameter("tableName", tableName)
                .getResultList();
    }

    @Transactional
    public void desactiverContraintes() {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        for (EntityType<?> entityType : entities) {
            Class<?> entityClass = entityType.getJavaType();
            String tableName = getTableName(entityClass);

            List<String> contraintes = listerContraintes(entityClass);

            for (String contrainte : contraintes) {
                String sql = "ALTER TABLE \"" + tableName + "\" DROP CONSTRAINT \"" + contrainte + "\"";
                try {
                    entityManager.createNativeQuery(sql).executeUpdate();
                } catch (Exception e) {
                    System.out.println("Erreur lors de la suppression de la contrainte : " + e.getMessage());
                }
            }
        }
    }

    @Transactional
    public void effacerDonneesToutesEntites(Utilisateur utilisateur) {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        for (EntityType<?> entityType : entities) {
            Class<?> entityClass = entityType.getJavaType();
            String tableName = this.getTableName(entityClass);

            if (!tableName.equals("utilisateur") && !tableName.equals("etat_construction") && !tableName.equals("etat_travaux")&& !tableName.equals("type_travail") && !tableName.equals("mois") && utilisateur.getProfil().equals(Profil.ADMIN)) {
                entityManager.createNativeQuery("TRUNCATE TABLE " + tableName + " CASCADE ").executeUpdate();
            }
        }
    }

    @Transactional
    public void reactiverContraintes() {
        for (String entityName : contraintesDesactivees.keySet()) {
            for (String contrainte : contraintesDesactivees.get(entityName)) {
                String sql = "ALTER TABLE " + entityName + " ENABLE CONSTRAINT " + contrainte;
                entityManager.createNativeQuery(sql).executeUpdate();
            }
        }
        contraintesDesactivees.clear();
    }
}