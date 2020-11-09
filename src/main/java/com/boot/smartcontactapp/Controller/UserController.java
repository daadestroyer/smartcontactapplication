package com.boot.smartcontactapp.Controller;

import com.boot.smartcontactapp.Entities.Contact;
import com.boot.smartcontactapp.Entities.User;
import com.boot.smartcontactapp.Helper.Message;
import com.boot.smartcontactapp.Repo.ContactRepository;
import com.boot.smartcontactapp.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String user_email = principal.getName();
        //System.out.println("Username : "+user_email);

        // getting data from database using user email
        User user = this.userRepository.getUserByUserName(user_email);

        //System.out.println(user);
        model.addAttribute("user", user);

    }

    // dashboard home
    @GetMapping("/dashboard")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    // add contact handler
    @GetMapping("/add-contact")
    public String addContact(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add-contact";
    }

    // save contact handler
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute("contact") Contact contact,
                                 @RequestParam("profileImage") MultipartFile multipartFile,
                                 Principal principal,
                                 HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);


            // processing and uploading file
            if (multipartFile.isEmpty()) {
                // if file is empty
                System.out.println("File is empty");
            } else {
                // if not then save to particular folder
                contact.setImage(multipartFile.getOriginalFilename());
                File file = new ClassPathResource("static/img").getFile();
                Path path = Paths.get(file.getAbsolutePath() + File.separator + "(" + user.getId() + ")" + multipartFile.getOriginalFilename());
                Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            }
            // contact me user store kia
            contact.setUser(user);
            // user me contact store kia
            user.getContacts().add(contact);
            // saving data
            this.userRepository.save(user);

            // success message
            session.setAttribute("message", new Message("Contact Added Successfully !!!", "alert-success"));

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            e.printStackTrace();
            session.setAttribute("message", new Message("Something Went Wrong Try Again !!!" + e.getMessage(), "alert-danger"));

        }
        return "normal/add-contact";

    }

    // view contacts
    @GetMapping("/view-contacts")
    public String viewContact(Model model, Principal principal) {
        model.addAttribute("title", "View Contacts");

        String userName = principal.getName();

        User user = this.userRepository.getUserByUserName(userName);

        List<Contact> contacts = this.contactRepository.findContactsByUser(user.getId());
        model.addAttribute("contacts", contacts);
        return "normal/view-contacts";
    }

}
