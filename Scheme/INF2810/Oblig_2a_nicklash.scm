;; Prekode til oblig 2a i INF2810 2015: Prosedyrer for å jobbe med
;; Huffman-trær, fra seksjon 2.3.4 i SICP.

;; Abstraksjonsbarriere:

(define (make-leaf symbol weight)
  (list 'leaf symbol weight))

(define (leaf? object)
  (eq? (car object) 'leaf))

(define (symbol-leaf x) (cadr x))

(define (weight-leaf x) (caddr x))

(define (make-code-tree left right)
  (list left
        right
        (append (symbols left) (symbols right))
        (+ (weight left) (weight right))))

(define (left-branch tree) (car tree))

(define (right-branch tree) (cadr tree))

(define (symbols tree)
  (if (leaf? tree)
      (list (symbol-leaf tree))
      (caddr tree)))

(define (weight tree)
  (if (leaf? tree)
      (weight-leaf tree)
      (cadddr tree)))


;; Dekoding:

(define (decode bits tree)
  (define (decode-1 bits current-branch)
    (if (null? bits)
        '()
        (let ((next-branch
               (choose-branch (car bits) current-branch)))
          (if (leaf? next-branch)
              (cons (symbol-leaf next-branch)
                    (decode-1 (cdr bits) tree))
              (decode-1 (cdr bits) next-branch)))))
  (decode-1 bits tree))

(define (choose-branch bit branch)
  (if (= bit 0) 
      (left-branch branch)
      (right-branch branch)))


;; Sortering av node-lister:

(define (adjoin-set x set)
  (cond ((null? set) (list x))
        ((< (weight x) (weight (car set))) (cons x set))
        (else (cons (car set)
                    (adjoin-set x (cdr set))))))

(define (make-leaf-set pairs)
  (if (null? pairs)
      '()
      (let ((pair (car pairs)))
        (adjoin-set (make-leaf (car pair)
                               (cadr pair))
                    (make-leaf-set (cdr pairs))))))


;; Diverse test-data:

(define sample-tree
  (make-code-tree 
   (make-leaf 'ninjas 8) 
   (make-code-tree 
    (make-leaf 'fight 5) 
    (make-code-tree 
     (make-leaf 'night 1) 
     (make-leaf 'by 1)))))

(define sample-code '(0 1 0 0 1 1 1 1 1 0))

;;oppg 1a
(define (p-cons x y)
	(lambda (proc) (proc x y)))

(define (p-car proc)
    (proc (lamda (x y) x)))

(define (p-cdr proc)
	(proc (lambda (x y) y)))
	
;;resultater
;;(p-cons "foo" "bar")
;;#<procedure>

;;(p-car (p-cons "foo" "bar"))
;;"foo"

;;(p-cdr (p-cons "foo" "bar"))
;;"bar"

;;(p-car (p-cdr (p-cons "zoo" (p-cons "foo" "bar"))))
;;"foo"

;;oppg 1b
(define foo 30)

((lambda (x) ((lambda (y) (+ x y foo)) 20)) foo)
;;80

((lambda (foo) ((lambda (x) ((lambda (y) (+ foo x y)) 20)) foo)) 10)
;;40

;;oppg 1c

(define a1 (list 1 2 3 4))
(define a2 (list + - * /))
(define a3 (list 5 6 7 8))
(map (lambda (x y z) (y x z)) a1 a2 a3)
;;Det som skjer er at elementene blir satt sammen med de 
;;elementene på samme plass som seg selv i de andre listene
;;(1 + 5), (2 - 6), (3 * 7) og (4 / 8)
;;Resultatet blir da (6 -4 21 1/2)

;;oppg 2a

(define (member? sym l1)
	(cond ((or (null? l1) (null? sym)) #f)
		((eq? sym (car l1))#t)
		(else (member? sym (cdr l1)))))
		
;;(member? 'forest '(lake river ocean))
;;#f

;;(member? 'river '(lake river ocean))
;;#t
		
;;oppg 2b
;;Den indre prosedyren gjør det mulig å jobbe med den nåværende grenen fra treet.
;;Dette uten å endre på de orginale treet som vi da trenger intakt, 
;;og da beholder som en global variabel. Noe som da hadde blitt endret på ved kun
;;bruk av hovedprosedyren.

;;oppg 2c
(define (decode-tail bits tree)
	(define (decode-tail-iner bits current-branch l1)
		(if (null? bits)
			l1
			(let ((next-branch (choose-branch (car bits) current-branch)))
				(if (leaf? next-branch)
					(decode-tail-iner (cdr bits) tree (append l1 (list (symbol-leaf next-branch))))
					(decode-tail-iner (cdr bits) next-branch l1)))))
	(decode-tail-iner bits tree '()))
	
;;oppg 2d

(decode-tail sample-code sample-tree)
;;Resultatet av å kjøre denne koden var (ninjas fight ninjas by night)

;;oppg 2e

(define (encode liste-symbol tree)
	(define (encode-tail current-symbol current-tree current-bits)
		(cond ((or (null? current-symbol) (null? current-tree)) current-bits)
			((member? (car current-symbol) (symbols tree))
				(cond  ((member? (car current-symbol) (symbols (left-branch current-tree)))
					(if (and (leaf? (left-branch current-tree)) (eq? (car current-symbol) (symbol-leaf (left-branch current-tree))))
						(encode-tail (cdr current-symbol) tree (append current-bits '(0)))
						(encode-tail current-symbol (left-branch current-tree) (append current-bits '(0)))))
				((member? (car current-symbol) (symbols (right-branch current-tree)))
					(if (and (leaf? (right-branch current-tree)) (eq? (car current-symbol) (symbol-leaf(right-branch current-tree))))
					(encode-tail (cdr current-symbol) tree (append current-bits '(1)))
					(encode-tail current-symbol (right-branch current-tree) (append current-bits '(1)))))))
			(else current-bits)))
		(encode-tail liste-symbol tree '()))
		
;;ender med litt unødvendig dobbel sjekking

;;(decode (encode '(ninjas fight ninjas) sample-tree) sample-tree)
;;(ninjas fight ninjas)


;;oppg 2f
	
(define (grow-huffman-tree liste)
	(define (tree-iter tmp-t tmp-l)
		(if (= (length tmp-l) 1) tmp-t
			(let* ((Node (make-code-tree (car tmp-l) (cadr tmp-l)))
				(l1 (adjoin-set Node (cddr tmp-l))))
			(tree-iter Node l1))))
		(cond((null? liste) '())
			((= (length liste) 1) (make-leaf (car liste) (cdr liste)))
			(else (tree-iter '() (make-leaf-set liste)))))
			
;;alternativ

define (grow-huffman-tree liste)
	(define (successive-merge leaf-set) 
		(if (= (length leaf-set) 1) 
			(car leaf-set) 
			(let ((first (car leaf-set)) 
				(second (cadr leaf-set)) 
				(rest (cddr leaf-set))) 
				(successive-merge (adjoin-set (make-code-tree first second) 
                                       rest)))))
	(successive-merge liste))

	

;;oppg 2g
(define word-frequency '((ninjas 57) (samurais 20) (fight 45) (night 12) 
						(hide 3) (in 2) (ambush 2) (defeat 1) (the 5) (sword 4)
						(by 12) (assassin 1) (river 2) (forest 1) (wait 1) (poison 1)))

(define our-tree (grow-huffman-tree word-frequency))

(define all-sets '(ninjas fight 
					ninjas fight ninjas 
					ninjas fight samurais 
					samurais fight 
					samurais fight ninjas 
					ninjas fight by night))

(length (encode all-sets our-tree))
;;Resultatet blir 40 som vil si t koden bruker 40 bits.

(/ (length (encode all-sets our-tree)) (length all-sets))
;;resultatet blir 2.(6/17)

;;oppg 2h
(define (huffman-leaves tree)
	(define (huffman-leaves-iner current-branch)
		(if (null? current-branch)
			'()
			(if (leaf? current-branch)
				(list (cons (symbol-leaf current-branch) (weight-leaf current-branch)))
				(append (huffman-leaves-iner (left-branch current-branch)) (huffman-leaves-iner (right-branch current-branch))))))
	(cond ((null? tree) '())
			((leaf? tree) (list (symbol-leaf tree) (weight-leaf)))
			(else (huffman-leaves-iner tree))))

;;(huffman-leaves sample-tree)
;;((ninjas . 8) (fight . 5) (night . 1) (by . 1))
		
;;oppg 2i
(define (expected-codeword-length tree)
	(define (length-iner current-branch current-bits)
		(if (null? current-branch)
			current-bits
			(if (leaf? current-branch)
				(*(/(weight current-branch) (weight tree)) current-bits)
				(+ (length-iner (left-branch current-branch) (+ 1 current-bits)) (length-iner (right-branch current-branch) (+ 1 current-bits))))))
	(if (null? tree)
		0
		(length-iner tree 0)))
	
;;(expected-codeword-length sample-tree)
;;1.(3/5)
		
;;For å møtte den forventede gjennomsnitt lengden må det være en melding som består av ord med 
;;forkjellige fregvenser av lav, middels og høy. Ettersom øy frekvens vil gi lavere en forventet 
;;og lav frekvens vil gi høyere en forventet.