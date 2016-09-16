;;oppg 1a

(define make-counter
    (lambda ()
      (let ((count 0))
        (lambda () (set! count (+ 1 count)) count))))

(define count 42)
(define c1 (make-counter))
(define c2 (make-counter))
;;(c1) -> 1
;;(c1) -> 2
;;(c1) -> 3
;;count -> 42
;;(c2) -> 1


;;oppg 1b
;;vedlegg



;;oppg 2a
(define (make-stack start)
    (let ((my-stack start))
      (lambda (cmd . elem)
        (cond ((eq? cmd 'stack) my-stack)
              ((eq? cmd 'pop!)
               (if (not (null? my-stack))
                   (set! my-stack (cdr my-stack))))
              ((eq? cmd 'push!) (set! my-stack (append (reverse elem) my-stack)))))))



;;oppg 2b
(define (stack the-stack) (the-stack 'stack))
(define (pop! the-stack) (the-stack 'pop!))
(define (push! the-stack . new-elements)
    (for-each (lambda (element) (the-stack 'push! element)) new-elements))



;;oppg 3a
;;(define bar (list 'a 'b 'c 'd 'e))
;;
;;Tegning på vedlegg
;;
;;(set-cdr! (cdddr bar) (cdr bar))
;;
;;Tegning på vedlegg
;;
;;Ved å kalle på set-cdr gjør vi at posisjon 4 peker til posisjon 1, dette gjør at når vi kaller på
;;en posisjon, med list-ref som gir hva som er på en posisjon på en liste, som er høyere en 3 vil den 
;;bare gå i en løkke mellom 1 og 3. Dette gjør at 4 = b, 5 = c, 6 = d, 7 = b også videre i for alle 
;;input posisjoner på 4 eller over. 



;;oppg 3b
;;(define bah (list 'bring 'a 'towel))
;;
;;Tegning på vedlegg
;;
;;(set-car! bah (cdr bah))
;;
;;Tegning på vedlegg
;;
;;(set-car! (car bah) 42)
;;
;;Tegning på vedlegg
;;
;;Det blir slikt ettersom car og cdr nå peker til det samme stedet. Noe som gjør at vis det gjøres en endring på
;;en vil det gjøre endring på alle steder den er. Dette kan fint ses med omgjøringen av a til 42.




;;oppg 3c

(define (cycle? my-list)
    (define (cycle-iter work-list new-list)
      (cond ((null? work-list) #f)
            ((memq (cdr work-list) new-list) #t)
            (else (cycle-iter (cdr work-list) (append (list (cdr work-list)) new-list)))))
     (cycle-iter my-list '()))
  

;;En til mulighet er å bruker "tortoise and hare" alogritmen 
(define (cycle? my-list)
  (define (cycle-iter tortoise hare)
    (cond ((null? hare) #f)
          ((null? (cdr hare)) #f)
          ((eq? tortoise hare) #t)
          (else (cycle-iter (cdr tortoise) (cddr hare)))))
  (if (null? my-list) 
      #f
      (cycle-iter my-list (cdr my-list))))

;;(cycle? '(hey ho)) -> #f
;;(cycle? '(la la la)) -> #f
;;(cycle? bah) -> #f
;;(cycle? bar) -> #t



;;oppg 3d
;;En vanlig liste består av cons celler i en kjede som alltid ender med sitt siste cons som en tom liste, '(), 
;;ettersom bar aldri ender i en tom liste, kun går i sirkler, så vil den da lede til å gi false. Mens bah er en liste 
;;hvor car og cdr peker på det samme derfor vil den ende vil den da ved nok bruk av cdr ende i en tom liste, '(), som da gjør at den blir true. 



;;oppg 3e      

(define (make-ring list)
  (define (copy-list this-list)
    (if (null? this-list)
        '()
        (cons (car this-list) (copy-list (cdr this-list)))))
        
  (define (make-ring-iter this-list head)
    (if (= 1 (length this-list))
        (set-cdr! this-list head)
        (make-ring-iter (cdr this-list) head)))
  
  (if (null? list) 
      "empty list"
      (begin (let ((my-ring (copy-list list)))
               (make-ring-iter my-ring my-ring)
               
               (define (top)
                 (car my-ring))
               
               (define (left-rotate!)
                 (set! my-ring (cdr my-ring))
                 (top))
               
               (define (right-rotate!)
                 (define (iter head)
                   (if (eq? head (cdr my-ring))
                       (top)
                       (begin (left-rotate!) (iter head))))
                 (iter my-ring))
               
               (define (insert! element)
                 (begin (right-rotate!) (set-cdr! element (cdr my-ring))
                        (set-cdr! my-ring element)
                        (left-rotate!)))
               
               (define (delete!)
                 (begin (right-rotate!) (set-cdr! my-ring (cddr my-ring)) (left-rotate!)))
               
               (define (commander cmd . args)
                 (cond ((eq? cmd 'top) (top))
                       ((eq? cmd 'left-rotate!) (left-rotate!))
                       ((eq? cmd 'right-rotate!) (right-rotate!))
                       ((eq? cmd 'insert!) (insert! args))
                       ((eq? cmd 'delete!) (delete!))))
               commander))))
              
(define (top the-ring) (the-ring 'top))
(define (left-rotate! the-ring) (the-ring 'left-rotate!))
(define (right-rotate! the-ring) (the-ring 'right-rotate!))
(define (insert! the-ring new-element) (the-ring 'insert! new-element))
(define (delete! the-ring) (the-ring 'delete!))



;;Oppg 3
;;rotering til venstre vil ta inn en ring og opdatere dens top peker til cdr fra start ringen, 
;;dette gir en konstant tids kompleksitet på O(1). Mens rotering mot høyere vil da ha tidskompleksiteten O(n) ettersom 
;;den må gå igjennom alle elementene for å sette top til det siste elementet.