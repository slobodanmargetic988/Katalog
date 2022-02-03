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
import com.katalog.model.Proizvod;
import com.katalog.repository.KategorijaRepository;
import com.katalog.repository.ProizvodRepository;
import com.katalog.service.UserService;
import java.sql.CallableStatement;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Slobodan Margetic slobodanmargetic988@gmail.com
 */
@Scope(WebApplicationContext.SCOPE_REQUEST)
@RestController
@RequestMapping(value = "/rest")
public class MainRESTController {
    
    private static EntityManagerFactory factory = null;
    private static EntityManager entityManager = null;
    
    @Autowired
    private ProizvodRepository proizvodRepository;
    
    @Autowired
    private KategorijaRepository kategorijaRepository;
    
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
    
    @GetMapping("/dodajProizvod")
    public String dodajProizvod(Model model
    ) {
        return "main/dodajProizvod";
    }

    @GetMapping("/dodajKategoriju")
    public String dodajKategoriju(Model model
    ) {
        return "main/dodajKategoriju";
    }
    
    @RequestMapping(value = "/dodajProizvod", method = RequestMethod.POST)
    public String dodajProizvod(final Model model, final HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "naziv") String naziv,
            @RequestParam(name = "opis") String opis,
            @RequestParam(name = "cena") int cena,
            @RequestParam(name = "dostupno") int dostupno
    ) {
        Proizvod proizvod = new Proizvod();
        proizvod.setNaziv(naziv);
        proizvod.setOpis(opis);
        proizvod.setCena(cena);
        proizvod.setDostupno(dostupno);        
        try {
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.addproizvod( ?,?,?,? )}");
            proc.setString(1, proizvod.getNaziv());
            proc.setString(2, proizvod.getOpis());
            proc.setInt(3, proizvod.getCena());
            proc.setInt(4, proizvod.getDostupno());
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesno dodavanje proizvoda");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @RequestMapping(value = "/dodajKategoriju", method = RequestMethod.POST)
    public String dodajKategoriju(final Model model, final HttpServletRequest request,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "naziv") String naziv,
            @RequestParam(name = "opis") String opis
    ) {
        try {
            
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.addkategorija( ?,? )}");
            
            proc.setString(1, naziv);
            proc.setString(2, opis);
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesno dodavanje kategorije");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/pregledSvihProizvoda")
    public String pregledProizvoda(Model model
    ) {
        model.addAttribute("sviProizvodi", proizvodRepository.getallproizvodi());
        return "main/sviProizvodi";
    }
    
    @GetMapping("/pregledJednogProizvoda/{proizvodId}")
    public String pregledJednogProizvoda(Model model,
            @PathVariable final int proizvodId
    ) {
        model.addAttribute("proizvod", proizvodRepository.findFirstById(proizvodId));
        model.addAttribute("kategorije", kategorijaRepository.getallkategorije());
        
        return "main/proizvod";
    }

    @GetMapping("/pregledSvihKategorija")
    public String pregledSvihKategorija(Model model
    ) {
        model.addAttribute("sveKategorije", kategorijaRepository.getallkategorije());
        return "main/sveKategorije";
    }
    
    @GetMapping("/pregledJedneKategorije/{kategorijaId}")
    public String pregledJedeKategorije(Model model,
            @PathVariable final int kategorijaId
    ) {
        model.addAttribute("kategorija", kategorijaRepository.findFirstKategorijaById(kategorijaId));
        
        return "main/kategorija";
    }

    @GetMapping("/removeProizvod/{proizvodId}")
    public String removeProizvod(Model model, @PathVariable final int proizvodId,
            RedirectAttributes redirectAttributes
    ) {
        
        try {
            
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.removeproizvod( ? )}");
            proc.setInt(1, proizvodId);
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesno brisanje proizvoda");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }

    @GetMapping("/removeKategorija/{kategorijaId}")
    public String removeKategorija(Model model, @PathVariable final int kategorijaId,
            RedirectAttributes redirectAttributes
    ) {
        
        try {
            
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.removekategorija( ? )}");
            proc.setInt(1, kategorijaId);
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesno brisanje kategorije");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/editProizvod/{proizvodId}")
    public String editProizvod(Model model, @PathVariable final int proizvodId
    ) {
        model.addAttribute("proizvod", proizvodRepository.findFirstById(proizvodId));
        
        return "main/editProizvod";
    }
    
    @GetMapping("/editKategorija/{kategorijaId}")
    public String editKategorija(Model model, @PathVariable final int kategorijaId
    ) {
        model.addAttribute("kategorija", kategorijaRepository.findFirstKategorijaById(kategorijaId));
        
        return "main/editKategorija";
    }
    
    @PostMapping(value = "/editProizvod/{proizvodId}")
    public String editProizvod(final Model model,
            @PathVariable final int proizvodId,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "naziv") String naziv,
            @RequestParam(name = "opis") String opis,
            @RequestParam(name = "cena") int cena,
            @RequestParam(name = "dostupno") int dostupno
    ) {
        
        try {
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.updateproizvod( ?,?,?,?,? )}");
            proc.setInt(1, proizvodId);
            proc.setString(2, naziv);
            proc.setString(3, opis);
            proc.setInt(4, cena);
            proc.setInt(5, dostupno);
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesna izmena proizvoda");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @PostMapping(value = "/editKategorija/{kategorijaId}")
    public String editKategorija(final Model model,
            @PathVariable final int kategorijaId,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "naziv") String naziv,
            @RequestParam(name = "opis") String opis
    ) {
        
        try {
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.updatekategorija( ?,?,?)}");
            proc.setInt(1, kategorijaId);
            proc.setString(2, naziv);
            proc.setString(3, opis);
            
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesna izmena kategorije");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @PostMapping(value = "/dodajKategoriju/{proizvodId}")
    public String dodajKategoriju(final Model model,
            @PathVariable final int proizvodId,
            @RequestParam(name = "kategorijaId") int kategorijaId,
            RedirectAttributes redirectAttributes
    ) {
        
        try {
            
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.addkategorijazaproizvod( ?,? )}");
            proc.setInt(1, proizvodId);
            proc.setInt(2, kategorijaId);
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesna izmena proizvoda");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @GetMapping(value = "/ukloniKategorijuZaProizvod/{proizvodId}/{kategorijaId}")
    public String ukloniKategorijuZaProizvod(final Model model,
            @PathVariable final int proizvodId,
            @PathVariable final int kategorijaId,
            RedirectAttributes redirectAttributes
    ) {
        
        try {
            
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.removekategorijazaproizvod( ?,? )}");
            proc.setInt(1, proizvodId);
            proc.setInt(2, kategorijaId);
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesna izmena proizvoda");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @GetMapping(value = "/ukloniKategoriju/{kategorijaId}")
    public String ukloniKategoriju(final Model model,
            @PathVariable final int kategorijaId,
            RedirectAttributes redirectAttributes
    ) {
        
        try {
            
            Connection conn = createConn();
            CallableStatement proc = conn.prepareCall("{call webkatalog.removekategorija( ? )}");
            proc.setInt(1, kategorijaId);
            proc.execute();
            proc.close();
            redirectAttributes.addFlashAttribute("successMessage", "uspesno brisanje kategorije");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            System.out.println(e.getMessage());
            return "redirect:/";
        }
        
        return "redirect:/";
    }
    
    @GetMapping("/pretraga")
    public String pretraga(Model model
    ) {
        model.addAttribute("sveKategorija", kategorijaRepository.getallkategorije());
        return "main/pretraga";
    }

    @GetMapping("/pretraga/{kategorijaId}")
    public String pretragaPoKategoriji(Model model,
            @PathVariable final int kategorijaId
    ) {
        model.addAttribute("sveKategorija", kategorijaRepository.getallkategorije());
        model.addAttribute("rezultatPoKategoriji", kategorijaRepository.findFirstKategorijaById(kategorijaId));
        return "main/pretraga";
    }
    
    @PostMapping("/pretraga")
    public String pretragaPoNazivu(Model model,
            @RequestParam(name = "deonaziva") String deonaziva
    ) {
        model.addAttribute("sveKategorija", kategorijaRepository.getallkategorije());
        model.addAttribute("rezultatiPoNazivu", proizvodRepository.pretragaPoImenu(deonaziva));
        
        return "main/pretraga";
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////
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

