/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.katalog.controller;

import com.katalog.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.WebApplicationContext;
import com.katalog.configuration.KatalogUserPrincipal;
import com.katalog.repository.ProizvodRepository;
import com.katalog.service.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Slobodan
 */
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Controller
public class MainController {

    private static EntityManagerFactory factory = null;
    private static EntityManager entityManager = null;
    
  @Autowired
    private ProizvodRepository proizvodRepository;
  
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/loginTry")
    public String login(final Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return "main/registracija";
    }

    @GetMapping("/")
    public String homePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            User myUser = ((KatalogUserPrincipal) authentication.getPrincipal()).getUser();
            model.addAttribute("user", myUser);
        }

        try {
      System.out.println(proizvodRepository.getallproizvodi().get(0).getNaziv());

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "main/home";
    }

    @GetMapping("/uspesanlogin")
    public String homePageLogin(Model model,
            RedirectAttributes redirectAttributes
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getPrincipal().equals("anonymousUser")) {
            User myUser = ((KatalogUserPrincipal) authentication.getPrincipal()).getUser();

            User user = userService.findFirstByEmail(myUser.getEmail());
            model.addAttribute("userLogedIn", user);
        }

        redirectAttributes.addFlashAttribute("successMessageLogin", "Korisnik je uspesno prijavljen.");

        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(final Model model, final HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "prezime") String prezime,
            @RequestParam(name = "ime") String ime,
            @RequestParam(name = "email") String email,
            @RequestParam(name = "password") String lozinka,
            @RequestParam(name = "lozinkaRepeat") String lozinkaRepeat,
            @RequestParam(name = "adresa") String adresa,
            @RequestParam(name = "mesto") String mesto,
            @RequestParam(name = "postanskibroj") String postanskibroj,
            @RequestParam(name = "telefon") String telefon
    ) {
        if (userService.findFirstByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Korisnik sa unetom email adresom već postoji.");

            return "redirect:/registracija";
        }

        User user = new User();
        user.setIme(ime);
        user.setPrezime(prezime);
        user.setEmail(email);
        user.setPassword(bCryptPasswordEncoder.encode(lozinka));
        user.setAdresa(adresa);
        user.setPostanski_broj(postanskibroj);
        user.setMesto(mesto);
        user.setBroj_telefona(telefon);
        user.setRole("SHOPPER");

        try {

            userService.save(user);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

            return "redirect:/loginTry";
        }

        return "redirect:/";
    }

    @GetMapping("/pregledSvihProizvoda")
    public String pregledProizvoda(Model model
    ) {

        return "main/sviProizvodi";
    }

    @GetMapping("/pregledJednogProizvoda/{proizvodId}")
    public String pregledJednogProizvoda(Model model,
            @PathVariable final int proizvodId
    ) {
        return "main/proizvod";
    }

    public Connection createConn() {
        String url = "jdbc:postgresql://localhost:5432/test1";

        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "1234");
// Ensure EscapeSyntaxCallmode property set to support procedures if no return value
        props.setProperty("escapeSyntaxCallMode", "callIfNoReturn");
        try {
            Connection conn = DriverManager.getConnection(url, props);
            return conn;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    ;
    
    
        public void createProcedureAddProizvod() {
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"addproizvod\"(IN innaziv character varying,IN inopis character varying,IN incena integer,IN indostupno integer)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "INSERT INTO webKatalog.proizvod (naziv,opis,cena,dostupna_kolicina) VALUES (innaziv,inopis,incena,indostupno);\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    ;
    
    

        public void createProcedureAddKategorija() {
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"addkategorija\"(IN innaziv character varying,IN inopis character varying)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "INSERT INTO webKatalog.kategorija (naziv,opis) VALUES (innaziv,inopis);\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    ;
            public void createProcedureAddKategorijaZaProizvod() {//mozda prvo uraditi proveru da li vec postoji mada bi to trebalo da frontend ne dozvoljava
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"addkategorijazaproizvod\"(IN inproizvodid integer,IN inkategorijaid integer)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "INSERT INTO webKatalog.veznatabela (id_proizvoda,id_kategorije) VALUES (inproizvodid,inkategorijaid);\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    ;
 public void createProcedureRemoveProizvod() {
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"removeproizvod\"(IN inid integer)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "DELETE FROM webKatalog.veznatabela WHERE id_proizvoda=inid;\n"
                    + "DELETE FROM webKatalog.proizvod WHERE id=inid;\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    ;
    
    

        public void createProcedureRemoveKategorija() {
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"removekategorija\"(IN inid integer)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "DELETE FROM webKatalog.veznatabela WHERE id_kategorije=inid;\n"
                    + "DELETE FROM webKatalog.kategorija WHERE id=inid;\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    ;
            public void createProcedureRemoveKategorijaZaProizvod() {//mozda prvo uraditi proveru da li vec postoji mada bi to trebalo da frontend ne dozvoljava
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"removekategorijazaproizvod\"(IN inproizvodid integer,IN inkategorijaid integer)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "DELETE FROM webKatalog.veznatabela WHERE id_kategorije=inkategorijaid AND id_proizvoda=inproizvodid;\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    ;
   public void createProcedureUpdateProizvod() {
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"updateproizvod\"(IN inid integer,IN innaziv character varying,IN inopis character varying,IN incena integer,IN indostupno integer)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "UPDATE webkatalog.proizvod SET naziv=innaziv, opis=inopis,cena=incena,dostupna_kolicina=indostupno where id=inid;\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    ;
    
    

        public void createProcedureUpdateKategorija() {
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"updatekategorija\"(IN inid integer,IN innaziv character varying,IN inopis character varying)\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "UPDATE webkatalog.kategorija SET naziv=innaziv, opis=inopis where id=inid;\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
;
        
        
           public void createProcedureGetAllProizvodi() {
        try {
            Connection conn = createConn();
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"getallproizvodi\"()\n"
                    + "    LANGUAGE 'plpgsql'\n"
                    + "    \n"
                    + "AS $BODY$\n"
                    + "BEGIN\n"
                    + "SELECT * FROM webkatalog.proizvod;\n"
                    + "\n"
                    + "END;\n"
                    + "$BODY$;");
            stmt.close();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
;
}

/*
    try {  
       Connection conn = createConn();
      Statement stmt = conn.createStatement();
stmt.execute("CREATE OR REPLACE PROCEDURE webkatalog.\"testproc5\"(IN inime character varying)\n" +
"    LANGUAGE 'plpgsql'\n" +
"    \n" +
"AS $BODY$\n" +
"BEGIN\n" +
"INSERT INTO webKatalog.users (ime) VALUES (inime);\n" +
"\n" +
"END;\n" +
"$BODY$;");
stmt.close();
conn.setAutoCommit(true);

// Procedure call with transaction
CallableStatement proc = conn.prepareCall("{call webkatalog.testproc5( ? )}");

proc.setString(1, "x");
proc.execute();
proc.close();

                }catch(Exception e){System.out.println(e.getMessage());}
*/

/*List<Actor> actors = this.jdbcTemplate.query(
        "select first_name, last_name from t_actor",
        new RowMapper<Actor>() {
            public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {
                Actor actor = new Actor();
                actor.setFirstName(rs.getString("first_name"));
                actor.setLastName(rs.getString("last_name"));
                return actor;
            }
        });
*/