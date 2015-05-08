package reg.bean;

import java.io.ByteArrayInputStream;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import db.jpa.controllers.UserPhotoJpaController;

@ManagedBean(name = "imgBean")
@ApplicationScoped
public class ImageBean {

	private static UserPhotoJpaController userPhotoJpaController;

	static {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("registration-dao");
		userPhotoJpaController = new UserPhotoJpaController(factory);
	}

	public StreamedContent getImage() {
		FacesContext fc = FacesContext.getCurrentInstance();
		if (fc.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		}

		String id = fc.getExternalContext().getRequestParameterMap().get("photo_id");
		byte[] photoData = userPhotoJpaController.findUserPhoto(Integer.parseInt(id)).getPhotoData();
		return new DefaultStreamedContent(new ByteArrayInputStream(photoData));
	}

}
