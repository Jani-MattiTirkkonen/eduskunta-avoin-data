package eduskunta.avoin.data;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author tainalep
 */
public class EduskuntaAvoinData {

    public static void main(String[] args) {

        //Set<String> asTyypit = new HashSet<>();
        // TODO: tarkasta, että alikansiot on olemassa, jos ei niin luo ne
        //CreateFolders cf = new CreateFolders();
        //
        // tarkastetaan config tiedoston sisältö
        Long lastId = -1L;
        try (Scanner config = new Scanner(new File("config.txt"))) {
            lastId = Long.parseLong(config.nextLine());
        } catch (Exception e) {
            System.out.println("Virhe config tiedoston lukemisessa. " + e.toString());
        }

        // kysytään käyttäjältä millä numerolla aloitetaan
        Scanner lukija = new Scanner(System.in);
        System.out.println("Viimeksi jäätiin id arvoon: " + lastId);
        System.out.print("Mistä id arvosta aloitetaan nyt haku? ");
        Long currentId = Long.parseLong(lukija.nextLine());

        // kysytään myös montako kertaa aineistoa haetaan (tulee aina 100 kerralla)
        System.out.print("Kuinka monta hakua tehdään? (yksi haku on 100 aineistoa) ");
        int repeat = Integer.parseInt(lukija.nextLine());

        ReadApi apiReader = new ReadApi();

        // api antaa vain 100 riviä kerralla - tehdää haku x kertaa
        for (int i = 0; i < repeat; i++) {

            //Long newCurrentId = apiReader.getNextSetFromVaski(currentId, asTyypit);
            Long newCurrentId = apiReader.getNextSetFromVaski(currentId);

            if (Objects.equals(newCurrentId, currentId)) {
                i = repeat;
                System.out.println("Ei enää uudempia aineistoja");
            } else {
                currentId = newCurrentId;
            }

            System.out.println("Hauista suoritettuna: " + (i + 1) + "/" + repeat);

        }

        // tulostetaan viimeinen haettu pkarvo käyttäjälle  
        System.out.println("Viimeinen luettu id arvo oli: " + currentId);

        // tallennetaan se myös tiedostoon
        try {
            PrintWriter writer = new PrintWriter("config.txt", "UTF-8");
            writer.println(currentId);
            writer.close();
        } catch (Exception ex) {
            System.out.println("Virhe config tiedoston tallennuksessa. " + ex.toString());
        }

        //System.out.println("Löydetyt asiakirjatyypit:");
        //asTyypit.stream().forEach(a -> System.out.println(a));
    }

}
