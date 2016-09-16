;;; "Metacicular evaluator", basert på koden i seksjon 4.1.1-4.1.4 i SICP.
;;; Del av innlevering 3b i INF2810, vår 2015.
;; 
;; Last hele filen inn i Scheme. For å starte read-eval-print loopen og 
;; initialisere den globale omgivelsen, kjør:
;; (set! the-global-environment (setup-environment))
;; (read-eval-print-loop)
;;
;; Merk at det visse steder i koden, som i `special-form?', vanligvis
;; ville være mere naturlig å bruke `or' enn `cond'. Evaluatoren er
;; skrevet helt uten bruk av `and' / `or' for å vise at disse likevel
;; kan støttes i det implementerte språket selv om de ikke brukes i
;; implementeringsspråket. (Se oppgave 3a for mer om dette.)

;; hack for å etterlikne SICPs feilmeldinger:
(define exit-to-toplevel 'dummy)
(call-with-current-continuation 
 (lambda (cont) (set! exit-to-toplevel cont)))

(define (error reason . args)
  (display "ERROR: ")
  (display reason)
  (for-each (lambda (arg) 
              (display " ")
              (write arg))
            args)
  (newline)
  (exit-to-toplevel))


;;; Selve kjernen i evaluatoren (seksjon 4.1.1, SICP):
;;; -----------------------------------------------------------------------

;; Merk at vi skiller ut evaluering av special forms i en egen prosedyre.

(define (mc-eval exp env) ;; tilsvarer eval i SICP
  (cond ((self-evaluating? exp) exp)
        ((variable? exp) (lookup-variable-value exp env))
        ((special-form? exp) (eval-special-form exp env))
        ((application? exp)
         (mc-apply (mc-eval (operator exp) env)
                   (list-of-values (operands exp) env)))
        (else
         (error "Unknown expression type -- mc-eval:" exp))))

(define (mc-apply proc args) ;; tilsvarer apply i SICP
  (cond ((primitive-procedure? proc)
         (apply-primitive-procedure proc args))
        ((compound-procedure? proc)
         (eval-sequence
          (procedure-body proc)
          (extend-environment
           (procedure-parameters proc)
           args
           (procedure-environment proc))))
        (else
         (error
          "Unknown procedure type -- mc-apply:" proc))))

(define (eval-special-form exp env)
  (cond ((quoted? exp) (text-of-quotation exp))
        ((assignment? exp) (eval-assignment exp env))
        ((definition? exp) (eval-definition exp env))
        ((if? exp) (eval-if exp env))
        ((or? exp) (eval-or exp env)) ;; del av oppg 3a
        ((and? exp) (eval-and exp env)) ;; del av oppg 3a
        ((let? exp) (eval-let exp env)) ;; del av oppg 3c
        ((while? exp) (eval-while exp env)) ;; del av oppg 3e
        ((lambda? exp)
         (make-procedure (lambda-parameters exp)
                         (lambda-body exp)
                         env))
        ((begin? exp) 
         (eval-sequence (begin-actions exp) env))
        ((cond? exp) (mc-eval (cond->if exp) env))))

(define (special-form? exp)
  (cond ((quoted? exp) #t)  
        ((assignment? exp) #t)
        ((definition? exp) #t)
        ((if? exp) #t)
        ((lambda? exp) #t)
        ((begin? exp) #t)
        ((cond? exp) #t)
        ((or? exp) #t) ;; del av oppg 3a
        ((and? exp) #t) ;; del av oppg 3a
        ((let? exp) #t) ;; del av oppg 3c
        ((while? exp) #t) ;; del av oppg 3e
        (else #f)))

(define (list-of-values exps env)
  (if (no-operands? exps)
      '()
      (cons (mc-eval (first-operand exps) env)
            (list-of-values (rest-operands exps) env))))

;;(define (eval-if exp env)
;;  (if (true? (mc-eval (if-predicate exp) env))
;;      (mc-eval (if-consequent exp) env)
;;      (mc-eval (if-alternative exp) env)))


(define (eval-sequence exps env)
  (cond ((last-exp? exps) (mc-eval (first-exp exps) env))
        (else (mc-eval (first-exp exps) env)
              (eval-sequence (rest-exps exps) env))))

(define (eval-assignment exp env)
  (set-variable-value! (assignment-variable exp)
                       (mc-eval (assignment-value exp) env)
                       env)
  'ok)

(define (eval-definition exp env)
  (define-variable! (definition-variable exp)
    (mc-eval (definition-value exp) env)
    env)
  'ok)

;;; Selektorene / aksessorene som definerer syntaksen til uttrykk i språket 
;;; (seksjon 4.1.2, SICP)
;;; -----------------------------------------------------------------------

(define (self-evaluating? exp)
  (cond ((number? exp) #t)
        ((string? exp) #t)
        ((boolean? exp) #t)
        (else #f)))

(define (tagged-list? exp tag)
  (if (pair? exp)
      (eq? (car exp) tag)
      #f))

(define (quoted? exp)
  (tagged-list? exp 'quote))

(define (text-of-quotation exp) (cadr exp))

(define (variable? exp) (symbol? exp))

(define (assignment? exp)
  (tagged-list? exp 'set!))

(define (assignment-variable exp) (cadr exp))

(define (assignment-value exp) (caddr exp))


(define (definition? exp)
  (tagged-list? exp 'define))

(define (definition-variable exp)
  (if (symbol? (cadr exp))
      (cadr exp)
      (caadr exp)))

(define (definition-value exp)
  (if (symbol? (cadr exp))
      (caddr exp)
      (make-lambda (cdadr exp)
                   (cddr exp))))


(define (lambda? exp) (tagged-list? exp 'lambda))

(define (lambda-parameters exp) (cadr exp))
(define (lambda-body exp) (cddr exp))

(define (make-lambda parameters body)
  (cons 'lambda (cons parameters body)))


(define (if? exp) (tagged-list? exp 'if))

(define (if-predicate exp) (cadr exp))

(define (if-consequent exp) (cadddr exp))
;;(define (if-consequent exp) (caddr exp))

(define (if-alternative exp)
  (if (not (null? (cdddr exp)))
      (cadddr exp)
      'false))

(define (make-if predicate consequent alternative)
  (list 'if predicate consequent alternative))


(define (begin? exp) (tagged-list? exp 'begin))

(define (begin-actions exp) (cdr exp))

(define (last-exp? seq) (null? (cdr seq)))
(define (first-exp seq) (car seq))
(define (rest-exps seq) (cdr seq))

(define (sequence->exp seq)
  (cond ((null? seq) seq)
        ((last-exp? seq) (first-exp seq))
        (else (make-begin seq))))

(define (make-begin seq) (cons 'begin seq))


(define (application? exp) (pair? exp))
(define (operator exp) (car exp))
(define (operands exp) (cdr exp))

(define (no-operands? ops) (null? ops))
(define (first-operand ops) (car ops))
(define (rest-operands ops) (cdr ops))


(define (cond? exp) (tagged-list? exp 'cond))

(define (cond-clauses exp) (cdr exp))

(define (cond-else-clause? clause)
  (eq? (cond-predicate clause) 'else))

(define (cond-predicate clause) (car clause))

(define (cond-actions clause) (cdr clause))

(define (cond->if exp)
  (expand-clauses (cond-clauses exp)))

(define (expand-clauses clauses)
  (if (null? clauses)
      'false                          ; no else clause
      (let ((first (car clauses))
            (rest (cdr clauses)))
        (if (cond-else-clause? first)
            (if (null? rest)
                (sequence->exp (cond-actions first))
                (error "ELSE clause isn't last -- COND->IF:"
                       clauses))
            (make-if (cond-predicate first)
                     (sequence->exp (cond-actions first))
                     (expand-clauses rest))))))


;;; Evaluatorens interne datastrukturer for å representere omgivelser,
;;; prosedyrer, osv (seksjon 4.1.3, SICP):
;;; -----------------------------------------------------------------------

(define (false? x)
  (cond ((eq? x 'false) #t)
        ((eq? x #f) #t)
        (else #f)))

(define (true? x)
  (not (false? x)))
;; (som i SICP-Scheme'en vi tar med true/false som boolske verdier.)

(define (make-procedure parameters body env)
  (list 'procedure parameters body env))

(define (compound-procedure? p)
  (tagged-list? p 'procedure))


(define (procedure-parameters p) (cadr p))
(define (procedure-body p) (caddr p))
(define (procedure-environment p) (cadddr p))


(define (enclosing-environment env) (cdr env))

(define (first-frame env) (car env))

(define the-empty-environment '())

;; En ramme er et par der car er variablene
;; og cdr er verdiene:
(define (make-frame variables values)
  (cons variables values))

(define (frame-variables frame) (car frame))
(define (frame-values frame) (cdr frame))

(define (add-binding-to-frame! var val frame)
  (set-car! frame (cons var (car frame)))
  (set-cdr! frame (cons val (cdr frame))))

(define (extend-environment vars vals base-env)
  (if (= (length vars) (length vals))
      (cons (make-frame vars vals) base-env)
      (if (< (length vars) (length vals))
          (error "Too many arguments supplied:" vars vals)
          (error "Too few arguments supplied:" vars vals))))

;; Søker gjennom listene av variabel-bindinger i første ramme og 
;; så bakover i den omsluttende omgivelsen. (Moro; to nivåer av 
;; interne definisjoner med gjensidig rekursjon.) 
(define (lookup-variable-value var env)
  (define (env-loop env)
    (define (scan vars vals) 
      ; paralell rekursjon på listene av symboler og verdier
      (cond ((null? vars)
             (env-loop (enclosing-environment env)))
            ((eq? var (car vars))
             (car vals))
            (else (scan (cdr vars) (cdr vals)))))
    (if (eq? env the-empty-environment)
        (error "Unbound variable:" var)
        (let ((frame (first-frame env)))
          (scan (frame-variables frame)
                (frame-values frame)))))
  (env-loop env))

;; Endrer bindingen av 'var' til 'val' i en omgivelse 
;; (gir feil dersom 'var' ikke er bundet):
(define (set-variable-value! var val env)
  (define (env-loop env)
    (define (scan vars vals)
      (cond ((null? vars)
             (env-loop (enclosing-environment env)))
            ((eq? var (car vars))
             (set-car! vals val))
            (else (scan (cdr vars) (cdr vals)))))
    (if (eq? env the-empty-environment)
        (error "Unbound variable -- SET!:" var)
        (let ((frame (first-frame env)))
          (scan (frame-variables frame)
                (frame-values frame)))))
  (env-loop env))

;; define-variable! legger til en ny binding mellom 'var' og 'val' 
;; i den første rammen i omgivelsen 'env':
(define (define-variable! var val env)
  (let ((frame (first-frame env)))
    (define (scan vars vals)
      (cond ((null? vars)
             (add-binding-to-frame! var val frame))
            ((eq? var (car vars))
             (set-car! vals val))
            (else (scan (cdr vars) (cdr vals)))))
    (scan (frame-variables frame)
          (frame-values frame))))


;;; Håndtering av primitiver og den globale omgivelsen (SICP seksjon 4.1.4)
;;; -----------------------------------------------------------------------

(define (setup-environment)
  (let ((initial-env
         (extend-environment (primitive-procedure-names)
                             (primitive-procedure-objects)
                             the-empty-environment)))
    (define-variable! 'true 'true initial-env)
    (define-variable! 'false 'false initial-env)
    (define-variable! 'nil '() initial-env)
    initial-env))

(define the-global-environment the-empty-environment)
;; For initialisering av den globale omgivelsen, se kommentar til slutt i fila.

(define (primitive-procedure? proc)
  (tagged-list? proc 'primitive))

(define (primitive-implementation proc) (cadr proc))

(define primitive-procedures
  (list (list 'car car)
        (list 'cdr cdr)
        (list 'cons cons)
        (list 'null? null?)
        (list 'not not)
        (list '+ +)
        (list '- -)
        (list '* *)
        (list '/ /)
        (list '= =)
        (list 'eq? eq?)
        (list 'equal? equal?)
        (list 'display 
              (lambda (x) (display x) 'ok))
        (list 'newline 
              (lambda () (newline) 'ok))
        (list '1+ (lambda (x) (+ x 1))) ;; del av oppg 2a
        (list '1- (lambda (x) (- x 1))) ;; del av oppg 2a
        ;;      her kan vi legge til flere primitiver.
        (list '< <)
        (list '> >)
        ))

(define (primitive-procedure-names)
  (map car
       primitive-procedures))

(define (primitive-procedure-objects)
  (map (lambda (proc) (list 'primitive (cadr proc)))
       primitive-procedures))

(define apply-in-underlying-scheme apply)

(define (apply-primitive-procedure proc args)
  (apply-in-underlying-scheme
   (primitive-implementation proc) args))


;;; Hjelpeprosedyrer for REPL-interaksjon (SICP seksjon 4.1.4)
;;; -----------------------------------------------------------------------

(define input-prompt ";;; MC-Eval input:")
(define output-prompt ";;; MC-Eval value:")

(define (read-eval-print-loop) ;;tilsvarer driver-loop i SICP
  (prompt-for-input input-prompt)
  (let ((input (read)))
    (let ((output (mc-eval input the-global-environment)))
      (announce-output output-prompt)
      (user-print output)))
  (read-eval-print-loop))

(define (prompt-for-input string)
  (newline) (newline) (display string) (newline))

(define (announce-output string)
  (newline) (display string) (newline))

(define (user-print object)
  (if (compound-procedure? object)
      (display (list 'compound-procedure
                     (procedure-parameters object)
                     (procedure-body object)
                     '<procedure-env>))
      (display object)))

'METACIRCULAR-EVALUATOR-LOADED

;;; For å starte read-eval-print loopen og initialisere 
;;; den globale omgivelsen, kjør:
;;; (set! the-global-environment (setup-environment))
;;; (read-eval-print-loop)

;;Oppgaver

;;oppg 1

;;;MC-Eval input:
;;(define (foo cond else)
;;	(cond ((= cond 2) 0)
;;	(else (else cond))))
;;; MC-eval value:
;; ok
;;
;; Vi tar å definerer 3 forskjellige variabler, hvor en av dem 
;; er navnet på prosedyre. Ettersom vi jobber i vårt egne miljø 
;; (environment) så kan vi bruke navnet til scheme variabler uten 
;; at det krasjer ettersom det da vil si at rekkefølgen som 
;; avgjør dette. 
;; I vårt tilelle så blir cond evaluert mot 2, dette 
;; bestemer da hva som gjøres avhengig av hva som blir gitt som 
;; cond.


;;;MC-Eval input:
;; (define cond 3)
;;; MC-eval value:
;; ok

;; Neste del setter cond til å ha verdien 3. Siden dette da skjer 
;; utenfor den forrige prosedyren vi da dette ikke krasje med den 
;; ettersom denne bare blir kjørt ved kallet på cond


;;;MC-Eval input:
;; (define (else x) (/x 2))
;; (define (square x) (* x x))
;;; MC-eval value:
;; ok

;; Detter blir bare helt vanlige prosedyrer, eneste forskjellen 
;; er de har innebygd Scheme navn.


;;;MC-Eval input:
;; (foo 2 square)
;;; MC-eval value:
;; ok

;; Dette er et kall på prosedyren foo med cond som 2 og else som 
;; prosedyren square. her er cond og else gitt som lokale variabler så
;; de overskygger de globale versionene. 
;; Den sjekker da cond (2) mot 2 om de er like
;; siden det da ser sant (#t) som returnerer den 0. 


;;;MC-Eval input:
;; (foo 4 square)
;;; MC-eval value:
;; ok

;; Igjen så er dette et kall på foo med 4 som cond og square som 
;; else. Den tester cond (4) om den er lik 2, som den ikke er, så 
;; returnerer den svaret til prosedyren else (square) med cond  
;; (4) som da et 16.


;;;MC-Eval input:
;;(cond ((= cond 2) 0)
;;	(else (else 4)))
;;; MC-eval value:
;; ok

;; Her kjøres da bare en enkel test på likheten mellom
;; tidligere definert cond (3) og 2. Dette gir da #f og vil kjøre 
;; tidligere definert prosedyre else (/ x 2) med x som 4. 
;; dette vil da returnere 2. 

;; Dette evalueres slikt, som tidligere nevnt, pga rekkefølegen
;; av cond'ene og else'ene. Den leser da cond og tester den mot special-form
;; som den er, etter det leser den = som en primitiv, dette gjør at neste 
;; cond blir lest som en variabel. 


;;oppg 2b

(define (install-primitive! proc-name proc-body)
  (set! primitive-procedures (append primitive-procedures (list (list proc-name proc-body)))))

(install-primitive! 'square (lambda (x) (* x x)))

;;alternative
;;(define (install-primitive! var val)
;;  (define-variable! var (list 'primitive val) the-global-environment))

;;Oppg 3a
;;(define (or? exp)
;;  (eq? (car exp) 'or))
;;
;;(define (and? exp)
;;  (eq? (car exp) 'and))

(define (or? exp) (tagged-list? exp 'ofr))
(define (and? exp) (tagged-list? exp 'and))

(define (eval-or exp env)
  (define (or-iter exp-iter)
    (if (mc-eval (car exp-iter) env)
        (mc-eval (car exp-iter) env)
        (if (null? (cdr exp-iter))
            'false
            (or-iter (cdr exp-iter)))))
  (if (null? (cdr exp))
      'false
      (or-iter (cdr exp))))

(define (eval-and exp env)
  (define (eval-and-iter exp-iter)
    (if (null? exp-iter)
        #t
        (if (mc-eval (car exp-iter) env)
            (eval-and-iter (cdr exp-iter))
            #f)))
  (eval-and-iter (cdr exp)))

;;alternativ for and
;;(define (eval-and exp env)
;;  (define (and-iter exp-iter)
;;    (if (null? (cdr exp-iter))
;;        (mc-eval (car exp-iter) env)
;;        (and (mc-eval (car exp-iter) env)
;;             (and-iter (cdr exp-iter)))))
;;  (if (null? (cdr exp))
;;      'true
;;      (and-iter (cdr exp))))

;; oppg 3b
(define (else? exp) (tagged-list? exp 'else))
(define (if-else exp) (cadr exp))

(define (no-else? exp)
  (cond ((null? exp) #t)
        ((else? exp) #f)
        (else (no-else? (cdr exp)))))

(define (eval-if exp env)
  (define (eval-if-iter exp-iter)
    (cond ((else? exp-iter) (mc-eval (if-else exp-iter) env))
          ((true? (mc-eval (if-predicate exp-iter) env))
           (mc-eval (if-consequent exp-iter) env))
          (else (eval-if-iter (cddddr exp-iter)))))
  (if (no-else? exp)
      (error "no else clause")
      (eval-if-iter exp)))

;;Alternativ ved antagelsen av at else alltid er med
;;(define (eval-if exp env)
;;  (cond ((else? exp) (mc-eval (if-else exp) env))
;;        ((true? (mc-eval (if-predicate exp) env))
;;         (mc-eval (if-consequent exp) env))
;;        (else (eval-if (cddddr exp) env))))

;;oppg 3c

;; 3d erstatter denne

(define (let? exp) (tagged-list? exp 'let))

#|(define (eval-let exp env)
  (display (let-body exp))
  (newline)
  (cons (append (list 'lambda
                       (let-variables exp))
                       (let-body exp))
                 (let-values exp)))

(define (let-variables exp)
  (map car (cadr exp)))

(define (let-values exp)
  (map cadr (cadr exp)))

(define (let-body exp)
  (cddr exp))|#

;;oppg 3d

(define (eval-let exp env)
  (mc-eval (cons (append (list 'lambda
                               (let-variables exp))
                         (let-body exp))
                 (let-values exp)) env))

(define (let-variables exp)
  (cond ((eq? (cadr exp) 'in) '())
        ((eq? (cadr exp) '=) (cons (car exp) (let-variables (cddr exp))))
        (else (let-variables (cdr exp)))))

(define (let-values exp)
  (cond ((eq? (car exp) 'in) '())
        ((eq? (car exp) '=) (cons (cadr exp) (let-values (cdr exp))))
        (else (let-values (cdr exp)))))

(define (let-body exp)
  (cond ((eq? (car exp) 'in) (cdr exp))
        (else (let-body (cdr exp)))))


;;oppg 3e

(define (while? exp) (tagged-list? exp 'while))
(define (while-start exp) (cadr exp))
(define (while-pred exp) (caddr exp))
(define (while-body exp) (cadddr exp))
(define (while-increment) (caddddr exp))

;;Funker ikke som den skal, fant ikke bedre løsning
(define (eval-while exp env)
  (let ((while-start exp))
    (define (while-iter)
      (if ((mc-eval (while-pred exp) env))
          ((mc-eval (while-body exp) env) (while-increment exp) (while-iter))
          (display "while-done")))
    (while-iter)))
 
;;test: (while (i 0) (< i 10) (display "hello") (set! i (+ i 1)))
 



(set! the-global-environment (setup-environment))
(read-eval-print-loop)