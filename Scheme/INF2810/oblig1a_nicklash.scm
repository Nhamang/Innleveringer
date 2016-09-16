;;Oblig1a Nicklash

;;oppg 1a

(* (+ 2 2) 5) 
;;gir 20, detter er rett frem matte. Den løser da (+ 2 2) til 4
;; så tar den da (* 4 5) til 20

;;oppg 1b
(* (+ 2 2) (5))
;;Dette gir feilmelding pga koden leser 5 som en prosedyre som ikke er definert eller en special form.

;;oppg 1c
(* (2 + 2) 5)
;;Samme som i oppg 1b så blir 2 lest som en udefinert prosedyre med da (+ 2) som argumenter, 
;;som hadde gått vis den kalte på en eksisterene prosedyre.

;;oppg 1d
(define bar (/ 4 2))
bar
;;gir resultatet 2. Dette er fordi variabelen bar blir definer som 4/2
;;så når vi kaller på bar kaller vi egentlig på (/ 4 2)

;;oppg 1e
(- bar 2)
;;dette kan gi 2 forskjellige resultater.
;;1. bar: undefined;
;;2. 0 ettersom vi har da definert bar som (/ 4 2)

;;oppg 1f
(/ (* bar 3 4 1) bar)
;;Dette blir 12. 
;;(/ (* bar 3 4 1) bar) => (/ (* (/ 4 2) 3 4 1) (/ 4 2))
;; => (/ (* 2 3 4 1) (/4 2)) => (/ 24 2) => 12

;;oppg 2a
;;Syntaks feilen er (zero? (1 - 1)) som skulle vært (zero? (- 1 1)) 

;;Når denne feilen er endret vil "or" bli evaluert til "piff". 
;;Dette er fordi or returnere det første som er #t og etterom alt annet en #f er #t, hvis ingen #t er vil den gi #f
;;den bryter da evalueringen med å gi en retur med engang den treffer #t
.
;;Kjøringen av "and" vil gi #f. Dette gjøres ettersom "and" tester at alle sine utsagn
;;er #t for å kunne gi #t, hvis alt hadde vært riktig ville den gitt #t.

;;Ved bruk av "if" vil vi ha fått "poff" ettersom strukturen til "if" er
;;(if condition
;;   (then
;;   (else))
;;som vil si at den tester sine krav og hvis de er #t (alt annet en #f) vil den da kjøre "then"
;;hvis de blir #f kjører den "else". Then og else kan være alt som
;;et tall, en strenge, variabel, prosedyre, etc.
;;Dette følger da ikke en vanlig evaluering ettersom den da hopper over en av delene, enten "then" eller "else".

;;ingen av dem trenger da å være rekursiv.

;;oppg 2b

(define sign
  (lambda (n)
    (if (zero? n)
        0
        (if(negative? n)
           -1
           1))))

(define sign
  (lambda (n)
    (cond ((zero? n) 0)
          ((negative? n) -1)
          (else 1))))

;;oppg 2c
(define sign
  (lambda (n)
    (or (and (zero? n) 0)
        (and (positive? n) 1)
        (and (negative? n) -1))))

;;oppg 3a
(define add1
  (lambda (n)
    (+ n 1)))


(define sub1
  (lambda (n)
    (- n 1)))

;;oppg 3b
(define plus
  (lambda (x y)
    (if (zero? y)
        x
        (add1 (plus x (sub1 y))))))

;;oppg 3c
;;Prosedyren jeg har skrevet i (b) gir en rekursiv prosses.

(define plus
  (lambda (x y)
    (if (zero? y)
        x
        (plus (sub1 x) (add1 y)))))
;;iterativ prosses

;;Denne prosedytren (c) gir en iterative prossesen, i denne prossesen trenger vi ikke å vente på retur verdien siden den gir svaret direkte.
;;Med andre ord det er da ingen backtracking.
;;Denne (c) vil da se slik ut 
;;(plus 3 4) => (plus 4 3) => (plus 5 2) => (plus 6 1) => (plus 7 0) => 7 

;;I den føste (b) prosedyren vi nødt til å vente med add1
;;til plus har blitt evaluert,  som gjør at vi får en lineært økende stack, litt som
;;(plus 3 2) => (add1 (plus 3 1)) => (add1 (add1 (plus 3 0)))
;;=> (add1 (add1 3) => (add1 4) => 5


;;oppg 3d

(define power-close-to
    (lambda (b n)
      (define power-iter
        (lambda (e)
          (if (> (expt b e) n)
              e
              (power-iter (+ e 1)))))
      (power-iter 1)))
;;Den er forenklet med å droppe b og n som argumenter i power-iter. 
;;Dette lar seg jo gjøre ettersom at b og n er "bundet" til power-close-to som gjør at de er
;;frie variabeler som gjør det unødvendig å sende dem videre