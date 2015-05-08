package reg.bean;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.commons.io.IOUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import db.entities.UserDetails;
import db.entities.UserPhoto;
import db.jpa.controllers.UserDetailsJpaController;
import db.jpa.controllers.UserPhotoJpaController;

@ManagedBean(name = "rBean")
@ViewScoped
public class RegistrationMBean implements Serializable {

	private String name;
	private String address;
	private UploadedFile file;
	private byte[] byteData;
	private UserDetailsJpaController userDetailsJpaController;
	private UserPhotoJpaController userPhotoJpaController;
	private List<UserDetails> users;
	private UserDetails selectedUser;
	private StreamedContent content;

	public RegistrationMBean() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("registration-dao");
		userDetailsJpaController = new UserDetailsJpaController(factory);
		userPhotoJpaController = new UserPhotoJpaController(factory);
		users = userDetailsJpaController.findUserDetailsEntities();

	}

	public void handleFileUpload(FileUploadEvent event) {
		file = event.getFile();
		try {
			byteData = IOUtils.toByteArray(file.getInputstream());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addUser(ActionEvent event) {

		// add photo
		try {
			UserPhoto photo = new UserPhoto();
			photo.setPhotoName(file.getFileName());
			photo.setPhotoData(byteData);
			userPhotoJpaController.create(photo);
			UserDetails detailed = new UserDetails();
			detailed.setAddress(address);
			detailed.setUsername(name);
			detailed.setPhotoId(photo);
			userDetailsJpaController.create(detailed);
			RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage("User Added"));

		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<UserDetails> getUsers() {
		return users;
	}

	public void setUsers(List<UserDetails> users) {
		this.users = users;
	}

	public UserDetails getSelectedUser() {
		return selectedUser;
	}

	public void setSelectedUser(UserDetails selectedUser) {
		this.selectedUser = selectedUser;
	}

	public StreamedContent getContent() {
		byte[] data = this.selectedUser.getPhotoId().getPhotoData();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
		content=new DefaultStreamedContent(byteArrayInputStream, "image/jpg", this.selectedUser.getPhotoId().getPhotoName());
		return content;
	}

	public void setContent(StreamedContent content) {
		this.content = content;
	}

}
