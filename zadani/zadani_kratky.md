# Úkol

Naprogramovat puzzle hru podobnou jako je "LightBulb".

# Cíl

Cílem hry je otáčet políčka na hrací desce tak, aby se propojily vodiče a rozsvítily se všechny žárovky napájené z jednoho zdroje energie.

# Základní info

- Hrací deska: Mřížka o velikosti M x N
- Políčka
- - Vodiče - propojují strany políčka, existují 4 typy vodičů, na okrajích nesmí směřovat ven
- - Zdroj energie - je pouze jeden v celé hře, funguje i jako vodič
- - Žárovky - každá má jeden přívod energie, musí být alespoň jedna žárovka

# Start hry

Hra začíná s vyřešeným zapojením,
to znamená že všechny žárovky jsou už jakoby propojené se zdrojem, ale políčka jsou náhodně pootočená, takže vlastně jsou zapojený ale nejsou a nesvítí:D.

# Hra

Klikáním se otácí políčka o 90° doprava tak, aby se vytvořil funkční obvod od zdroje ke všem žárovkám a všechny se rozsvítily.

# Vizuál

Musíme barevně odlišit dráty a žárovky, které jsou aktuálně připojené ke zdroji, od těch nepřipojených.

# Obtížnosti

Hra má mít různé úrovně obtížnosti - mění se velikost desky, počet žárovek/vodičů.

# Nápověda - náhled

Možnost zobrazit druhé okno, které neumožňuje hrát hru, ale ukazuje u každého políčka, kolik otočení mu chybí do správné pozice.

# Statistiky

Zobrazení počtu potřebných vs. skutečně provedených otočení pro každé políčko (na konci nebo i během hry).

# Logování a Replay

Průběh hry se musí dát uložit do souboru. Z uloženého souboru musí jít hru znovu načíst a "přehrát" krok za krokem popředu i pozadu. Z režimu přehrávání musí být možné kdykoli přepnout zpět do režimu hraní a pokračovat.
