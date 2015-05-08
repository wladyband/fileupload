package daotest;

import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Test;

import db.entities.UserDetails;
import db.jpa.controllers.UserDetailsJpaController;
import db.jpa.controllers.UserPhotoJpaController;

public class DaoTest {

	@Test
	public void test() {
		EntityManagerFactory factory = Persistence
				.createEntityManagerFactory("registration-dao");
		 UserPhotoJpaController userPhotoJpaController = new UserPhotoJpaController(factory);
		 System.out.println(userPhotoJpaController.findUserPhotoEntities().size());
	}

}
