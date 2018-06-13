# Eduskunta avoin data
Java ohjelma [eduskunnan avoimen datan](http://avoindata.eduskunta.fi/) lataamiseksi omalle koneelle.

## Ohjelman toiminnan pääpiirteet
Ohjelma lukee APIsta VaskiData aineistoa läpi. Käynnistyessään kysyy mistä ID arvosta lähdetään liikkeelle ja montako kertaa 100 setin aineisto haetaan. 

Ohjelma tallentaa PDF tiedostot perustuslakivaliokunnan aineistosta (PeV) seuraavat asiakirjatyypit:
- Valiokunnan mietintö (eduskuntatunnus=PeVM)
- Valiokunnan lausunto (eduskuntatunnus=PeVL)
- Asiantuntijalausunto (asiakirjatyyppinimi=ssiantuntijalausunto)

Kaikki muut ohitetaan. 

### Asiantuntijalausunnot
Eduskunta ei syötä metatietoja asiantuntijalausunnoista, joten ne yritetään tunnistaa lukemalla PDF tiedoston sisältö ja mikäli siellä esiintyy perustuslakivaliokunta/ perustuslakivaliokunnan/ perustuslakivaliokunnalle tiedosto ladataan. 

## Ohjelman käyttö
Kopioi dist -kansio omalle koneellesi haluamaasi paikkaan. 

Ohjelma tarvitsee config.txt tiedoston, jossa on tiedo suurimmasta ladatusta ID arvosta

Ohjelma lataa tiedostot kolmeen alikansioon
- asiantuntija
- lausunto 
- mietinto

## Jatkokehitys
Mahdollisesti jatkokehitetään niin, että myös metadatat aineistosta tallennetaan. 

