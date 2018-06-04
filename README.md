# Eduskunta avoin data
Java ohjelma [eduskunnan avoimen datan](http://avoindata.eduskunta.fi/) lataamiseksi omalle koneelle.

## Ohjelman toiminnan pääpiirteet
Ohjelma lukee APIsta VaskiData aineistoa läpi. Käynnistyessään kysyy mistä ID arvosta lähdetään liikkeelle ja montako kertaa 100 setin aineisto haetaan. 

Ohjelma tallentaa PDF tiedostot perustuslakivaliokunnan aineistosta (PeV) seuraavat asiakirjatyypit:
- Valiokunnan mietintö 
- Valiokunnan lausunto
- Asiantuntijalausunto

Kaikki muut ohitetaan. 

## Ohjelman käyttö
Kopioi prog -kansio omalle koneellesi haluamaasi paikkaan. 

Ohjelma tarvitsee config.txt tiedoston, jossa on tiedo suurimmasta ladatusta ID arvosta

Ohjelma lataa tiedostot kolmeen alikansioon
- asiantuntija
- lausunto 
- mietintö

## Jatkokehitys
Mahdollisesti jatkokehitetään niin, että myös metadatat aineistosta tallennetaan. 

