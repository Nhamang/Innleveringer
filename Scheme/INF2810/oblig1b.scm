;;oppg 1a - e
;;vedlegg

;;oppg 1f
(define list1 (list 1 2 3 4))
(car (cdr (cdr list1)))

;;oppg 1g
(define list2 (list (list 1 2) (list 3 4)))
(car (car (cdr list2)))

;;oppg 1h
(define list3 (list (list 1) (list 2) (list 3) (list 4)))
(car (car (cdr (cdr list3))))

;;oppg 1i
;;cons
(cons (cons 1 (cons 2 '())) (cons (cons 3 (cons 4 '())) '()))

;;list
(list (list 1 2) (list 3 4))

;;Oppg 2a

(define (length2 x)
    (define (length-iter x x1)
      (if (null? x)
          x1
          (length-iter (cdr x) (+ x1 1))))
    (length-iter x 0))

;;oppg 2b

(define (rev-list n)
    (define (rev-iter x y)
      (if (null? x)
          y
          (rev-iter (cdr x) (cons (car x) y))))
    (rev-iter n '()))
;;Dette er en halerekusjon. Jeg velger å bruke denne for den tar da mindre plass og tid.

;;oppg 2c
(define (ditch x y)
  (cond ((null? y) '())
        ((= x (car y)) (ditch x (cdr y)))
        (else (cons (car y) (ditch x (cdr y))))))
;;dette er en halerekusjon. den bruker konstant minne og tiden vokser lineært med hensyn til n.

;;oppg 2d
(define (nth x y)
    (define (nth-iter a b)
      (if (null? b)
          #f
          (if (= x a)
              (car b)
              (nth-iter (+ a 1) (cdr b)))))
    (nth-iter 0 y))

;;oppg 2e
(define (where x y)
  (define (where-iter a b c)
    (if (null? b)
        #f
        (if (= a(car b))
            c
            (where-iter a (cdr b)(+ 1 c)))))
  (where-iter x y 1))

;;oppg 2f
(define (map2 f x y)
    (if (or (null? x) (null? y))
        '()
        (cons (f (car x) (car y))
              (map2 f (cdr x) (cdr y)))))

;;oppg 2g
;;Gjennomsnitt
(map2 (lambda (x y) (/ (+ x y) 2)) '( 1 2 3 4) '(3 4 5))

;;Even
(map2 (lambda (x y) (if (and (even? x) (even? y)) #t #f)) '(1 2 3 4) '(3 4 5))

;;oppg 2h
(define ((both? f) x y)
  (if (and (f x) (f y))
      #t
      #f))

;;oppg 2i
define (self f)
    (lambda (x) (f x x)))
