(load "prekode3a.scm")
;; Gruppe: sarujans, meandres & Nicklash

;; 1 Prosedyrer for bedre prosedyrer
;; a&b)

(define (mem cmd proc)
  (let ((table (make-table)))
    (cond ((eq? 'memoize cmd)
           (insert! '(*org-proc*) proc table)
           (lambda args
             (let ((x (lookup args table)))
               (if x
                   x
                   (let ((result (apply proc args)))
                     (insert! args result table)
                     result)))))
           ((eq? 'unmemoize cmd)
            (proc '*org-proc*)))))

;; TEST
(display "\nTests of 1. a&b):\n")
(display "\nTest: fib\n")
(set! fib (mem 'memoize fib))
(fib 3)
(fib 3)
(fib 2)
(fib 4)
(set! fib (mem 'unmemoize fib))
(fib 3)
(display "\nTest: test-proc\n")
(set! test-proc (mem 'memoize test-proc))
(test-proc)
(test-proc)
(test-proc 40 41 42 43 44)
(test-proc 40 41 42 43 44)
(test-proc 42 43 44)

;; c)

;; Den orginale fib lagrer verdiene til alle fibonacci helt opp til 
;; verdien den er kalt med, mens mem-fib da lagrer kun for den ene
;; verdien den blir gitt. Dette skjer hovedsaklig fordi det rekursive 
;; kallet blir utført på den opprinelige fib men ikke på mem-fib. Så 
;; når du regner opp til 3 vil den orginale kjøre gjennom 0, 1,  
;; 2 og sist 3. Mens mem-fib kun gjør det for kallet på 3.

;; d)
(define (greet . args)
  (let ((output '("good " "day" " " "friend" "\n"))
         (time (lookup-haystack 'time args))
         (title (lookup-haystack 'title args)))     
            (if time
                (set-car! (cdr output) time))
            (if title
                (set-car! (cdddr output) title))
            (for-each display output)))

(define (lookup-haystack key haystack)
	(cond ((null? haystack) #f)
              ((= (length haystack) 1) #f)
              ((eq? key (car haystack)) (cadr haystack))
              (else (lookup-haystack key (cddr haystack))))) 

;; TEST
(display "\nTests of 1. d):\n")
(greet)
(greet 'time "evening")
(greet 'title "sir" 'time "morning")
(greet 'time "afternoon" 'title "dear")

;; 2 Strømmer
;; a)

(define (stream-to-list strm . args)
  (define (inf-strm strm cnt)
           (if (= cnt 0)
               '()
               (cons (stream-car strm) (inf-strm (stream-cdr strm) (- cnt 1)))))
  (define (fin-strm strm)
              (if (stream-null? strm)
                  '()
                  (cons (stream-car strm) (fin-strm (stream-cdr strm)))))
  (if (null? args)
      (fin-strm strm)
      (inf-strm strm (car args))))


(define (list-to-stream lst)
  (if (null? lst)
      the-empty-stream
      (cons-stream (car lst) (list-to-stream (cdr lst)))))

;; TEST
(display "\nTests of 2. a):\n")
(list-to-stream '(1 2 3 4 5))
(stream-to-list (stream-interval 10 20))
(show-stream nats 15)
(stream-to-list nats 10)

;; b)
      
(define (stream-map proc . argstreams)
  (define (items-null? argstreams)
    (cond ((null? argstreams) #f)
          ((stream-null? (car argstreams)) #t)
          (else (items-null? (cdr argstreams)))))
  (if (items-null? argstreams)
      the-empty-stream
      (cons-stream 
       (apply proc (map stream-car argstreams))
       (apply stream-map
              (cons proc (map stream-cdr argstreams))))))

;; c)

;; Problemet med denne løsningen er at strømmer kan være uendelige i lengden
;; som for eksempel integers-starting-from. Denne er uendelig og har ingen duplikater, 
;; noe som gjør at memq aldri terminerer.

;; d)
(define (remove-duplicates stream)
  (if (stream-null? stream) 
      the-empty-stream
      (cons-stream (stream-car stream) (remove-duplicates (stream-filter (lambda (x) (not (equal? x (stream-car stream)))) (stream-cdr stream))))))

(define dup (cons-stream 1 (cons-stream 2 (cons-stream 1 (cons-stream 3 the-empty-stream)))))

;; TEST
(display "\nTests of 2. d):\n")
(stream-car (stream-cdr (stream-cdr dup)))
(define dup2(remove-duplicates dup))
(stream-car (stream-cdr (stream-cdr dup2)))

;; 2e

;; Ved kjøringen av (stream-ref x 5) tar vi å forcer promise'ene opp til 5. 
;; Når vi da kaller på (stream-ref 7) så har vi allerede forced 1-5 som da 
;; fører til at alt under ikke vises, kun 6 og 7. Alt dette forteller oss er
;; at r5rs benytter memoisering ved utsatt evaluering.

;; 2f

(define (mul-streams s1 s2)
  (stream-map * s1 s2))

(define factorials (cons-stream 1 (mul-streams nats factorials)))

(display "\nTest 2. f):\n")
(stream-ref factorials 5)
(display "\n")
(stream-ref factorials 6)