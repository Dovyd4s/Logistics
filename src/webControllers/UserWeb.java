package webControllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.Manager;
import entities.User;
import hibernate.HibernateCRUD;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;

@Controller
public class UserWeb {

    @RequestMapping(value = "users/getAll", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getAllUsers(){
        return HibernateCRUD.getAllUsers().toString();
    }

    @RequestMapping(value = "users/getCustomer/{id}", method = RequestMethod.GET )
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String getUserById(@PathVariable(name = "id")int id){
        Manager user = (Manager) HibernateCRUD.getUserById(id);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Manager.class, new GsonSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(user);
    }

}
