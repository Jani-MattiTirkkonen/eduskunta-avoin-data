# Eduskunta avoin data
Java ohjelma [eduskunnan avoimen datan](http://avoindata.eduskunta.fi/) lataamiseksi omalle koneelle.

## Ohjelman toiminnan pääpiirteet
Ohjelma lukee APIsta VaskiData aineistoa läpi. Käynnistyessään kysyy mistä ID arvosta lähdetään liikkeelle ja montako kertaa 100 setin aineisto haetaan. 

Ohjelma tallentaa PDF tiedostot perustuslakivaliokunnan aineistosta (PeV) seuraavat asiakirjatyypit:
- Valiokunnan mietintö (eduskuntatunnus=PeVM)
- Valiokunnan lausunto (eduskuntatunnus=PeVL)
- Asiantuntijalausunto (asiakirjatyyppinimi=asiantuntijalausunto)

Kaikki muut ohitetaan. 

Koodit löytyvät src -kansiosta, josta voi tutkia ohjelman tarkkaa suoritusta. 

### Asiantuntijalausunnot
Eduskunta ei syötä metatietoja asiantuntijalausunnoista, joten ne yritetään tunnistaa lukemalla PDF tiedoston sisältö ja mikäli siellä esiintyy perustuslakivaliokunta/ perustuslakivaliokunnan/ perustuslakivaliokunnalle tiedosto ladataan. 

## Ohjelman käyttö
Kopioi dist -kansio omalle koneellesi haluamaasi paikkaan. 
Ohjelma käynnistetään terminaalissa ko. hakemistossa komennolla
java -jar "eduskunta-avoin-data.jar" 

Ohjelma käyttää config.txt tiedostoa, jossa on tiedo suurimmasta ladatusta ID arvosta. Tämä kuitenkin toimii vain muistin apuna käyttäjälle, eikä ole pakollinen. 

Ohjelma lataa tiedostot kolmeen alikansioon
- asiantuntija
- lausunto 
- mietinto
(kansiot tulevat dist -kansion latauksessa mukana)

## Jatkokehitys
- ohjelma tarkastaa alikansioiden olemassa olon ja tarvittaessa luo ne
- metatietojen tallennus


