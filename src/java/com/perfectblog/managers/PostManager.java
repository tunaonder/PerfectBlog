/*
 * Created by Sait Tuna Onder on 2018.02.27  * 
 * Copyright © 2018 Sait Tuna Onder. All rights reserved. * 
 */
package com.perfectblog.managers;

import com.perfectblog.entityclasses.Post;
import com.perfectblog.entityclasses.User;
import com.perfectblog.sessionbeans.PostFacade;
import com.perfectblog.sessionbeans.UserFacade;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.primefaces.model.UploadedFile;

@Named(value = "postManager")
@SessionScoped
/**
 *
 * @author Onder
 */
public class PostManager implements Serializable {

    // Instance Variables (Properties)
    private UploadedFile file;
    private String message = "";
    private String postText = "";

    /*
    The @EJB annotation implies that the EJB container will perform an injection of the object
    reference of the UserFacade object into userFacade when it is created at runtime.
     */
    @EJB
    private UserFacade userFacade;

    /*
    The @EJB annotation implies that the EJB container will perform an injection of the object
    reference of the PhotoFacade object into userPhotoFacade when it is created at runtime.
     */
    @EJB
    private PostFacade postFacade;

    // Returns the uploaded file
    public UploadedFile getFile() {
        return file;
    }

    // Set the uploaded file
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    // Return the message
    public String getMessage() {
        return message;
    }

    // Set the message
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String clearErrorMessage() {
        message = "";
        return "NewPost?faces-redirect=true";
    }

    // Handle the upload of the selected file
    public String upload() {

        // Check if a file is selected
        if (file.getSize() == 0) {
            message = "You need to choose a file first!";
            return "";
        }

        String mimeFileType = file.getContentType();

        if (mimeFileType.startsWith("image/")) {
            // The uploaded file is an image file
            /*
            The subSequence() method returns the portion of the mimeFileType string from the 6th
            position to the last character. Note that it starts with "image/" which has 6 characters at
            positions 0,1,2,3,4,5. Therefore, we start the subsequence at position 6 to obtain the file extension.
             */
            String fileExtension = mimeFileType.subSequence(6, mimeFileType.length()).toString();

            String fileExtensionInCaps = fileExtension.toUpperCase();

            if (fileExtensionInCaps.endsWith("JPG") || fileExtensionInCaps.endsWith("JPEG") 
                    || fileExtensionInCaps.endsWith("PNG") || fileExtensionInCaps.endsWith("GIF")) {
                // File type is acceptable
            } else {
                message = "Selected file type is not a JPG, JPEG, PNG, or GIF!";
                return "";
            }
        } else {
            message = "Selected file to upload must be an image file of type JPG, JPEG, PNG or GIF!";
            return "";
        }

        storePhotoFile(file);
        message = "";

        return "index?faces-redirect=true";
    }

    // Cancel file upload
    public String cancel() {
        message = "";
        return "index?faces-redirect=true";
    }

    // Store the uploaded photo file and its thumbnail version and create a database record 
    public FacesMessage storePhotoFile(UploadedFile file) {
        try {


            /*
            InputStream is an abstract class, which is the superclass of all classes representing an input stream of bytes.
            Convert the uploaded file into an input stream of bytes.
             */
            InputStream inputStream = file.getInputstream();

            // Close the input stream and release any system resources associated with it
            inputStream.close();

            FacesMessage resultMsg;

            // Obtain the username of the logged-in user
            String user_name = (String) FacesContext.getCurrentInstance()
                    .getExternalContext().getSessionMap().get("username");

            // Obtain the object reference of the logged-in User object
            User user = userFacade.findByUsername(user_name);

            String uniqueFileName = user.getId() + "_" + 
                    postFacade.findPostsByUserID(user.getId()).size() + "_" + file.getFileName();
                    
            Post newPost = new Post();
            newPost.setPostText(postText);
            newPost.setImageFileName(uniqueFileName);
            newPost.setUserId(user);

            postFacade.create(newPost);

            List<Post> photoList = postFacade.findPostsByUserID(user.getId());
            
            Post post = photoList.get(photoList.size()-1);

            // Reconvert the uploaded file into an input stream of bytes.
            inputStream = file.getInputstream();

            inputStreamToFile(inputStream, post.getImageFileName());

            // Compose the result message
            resultMsg = new FacesMessage("Success!", "File Successfully Uploaded!");

            // Return the result message
            return resultMsg;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FacesMessage("Upload failure!",
                "There was a problem reading the image file. Please try again with a new photo file.");
    }

    /**
     * @param inputStream of bytes to be written into file with name targetFilename
     * @param targetFilename
     * @return the created file targetFile
     * @throws IOException
     */
    private File inputStreamToFile(InputStream inputStream, String targetFilename) throws IOException {

        /*
        inputStream.available() returns an estimate of the number of bytes that can be read from
        the inputStream without blocking by the next invocation of a method for this input stream.
        A memory buffer of bytes is created with the size of estimated number of bytes.
         */
        byte[] buffer = new byte[inputStream.available()];

        // Read the bytes of data from the inputStream into the created memory buffer. 
        inputStream.read(buffer);

        // Create targetFile with the given targetFilename in the ROOT_DIRECTORY    
        File targetFile = new File(Constants.FILES_ABSOLUTE_PATH, targetFilename);

        // A file OutputStream is an output stream for writing data to a file
        OutputStream outStream;

        /*
        FileOutputStream is intended for writing streams of raw bytes such as image data.
        Create a new FileOutputStream for writing to targetFile
         */
        outStream = new FileOutputStream(targetFile);

        // Write the inputStream from the memory buffer into the targetFile
        outStream.write(buffer);

        // Close the output stream and release any system resources associated with it. 
        outStream.close();

        // Return the created file targetFile
        return targetFile;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

}
