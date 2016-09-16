	#beklager om programmet er dårlig kommentert
	#kjører tes 1-7
	.globl sprinter
	.data

	pros: .byte 37
	arg: .long 16
	tegn: .byte 99
	string: .byte 115
	octal: .byte 111
	hex: .byte 120
	desimal: .byte 100
	legg_til: .long 0
	mellom: .byte 32
	nr0: .byte 48
	nr9: .byte 57
	teller: .long 0
	nummer: .long 0
	ant_plass: .long 0
	negativ: .long 0


	sprinter:							

		pushl %ebp
		movl %esp, %ebp
		xor %eax, %eax
		movl 8(%ebp), %ecx
		movl 8(%ebp), %ebx
		movl 12(%ebp), %edx
		movl $16, arg
	
	strcpy:
	
		movb (%edx), %al
		incl %edx
		cmpb pros, %al
		je samm_tegn
		movb %al, (%ecx)
		incl %ecx
		cmpb $0, %al	
		jz string_lengde
		jmp strcpy
	
	#sjekker lengden
	string_lengde:
		cmpb $0, (%ebx)
		je exit
		movb (%ebx), %dl
		incl %eax
		incl %ebx
		jmp string_lengde
	
	#sammenligner hvilket tegn som kommer etter % og sender den til tilsvarende "metode"
	samm_tegn:
		movb (%edx), %al
		cmpb %al, pros
		je prosent_met
		cmpb %al, tegn
		je tegn_met
		cmpb %al, string
		je string_met
		cmpb %al, hex
		je hex_met
		cmpb octal, %al
		je octal_met
		cmpb %al, desimal
		je desimal_met
		jmp strcpy



		
	
	
	#utføres hvis tegnet var %, legger % på ecx, øker ecx og edx så går tilbake til strcpy
	prosent_met:
		movb %al, (%ecx)
		incl %ecx
		incl %edx
		jmp strcpy
	
	#utføres hvis tegnet er c, 
	tegn_met:
		movl %ebp, %edi
		addl arg, %edi
		addl $4, arg
		movb (%edi), %al
		movb %al, (%ecx)
		incl %ecx
		incl %edx
		jmp strcpy
	
	#utføres hvis tegnet er S
	string_met:
		incl %edx
		movl %ebp, %edi
		addl arg, %edi
		movl (%edi), %edi
		addl $4, arg
		jmp legg_til_string

	legg_til_string:
		movb (%edi), %al
		incl %edi
		cmpb $0, %al
		jz strcpy
		movb %al, (%ecx)
		incl %ecx
		jmp legg_til_string
	#utføres hvis tegnet er x
	hex_met:
		incl %edx
		pushl %edx
		pushl %eax
		pushl %esi
		movl %ebp, %eax
		addl arg, %eax
		movl (%eax), %eax
		addl $4, arg
		movl $0, teller
		movl $16, %esi
		cmpl $0, %eax
		je ingen
		jmp hexal

	ingen:
		movb $48, (%ecx)
		incl %ecx
		jmp strcpy

	hexal:
		cmpl $0, %eax
		jz legg_til_hex_tall
		movl $0, %edx
		divl %esi
		pushl %edx
		addl $1, teller
		incl nummer
		jmp hexal

	legg_til_hex_tall:
		cmpl $0, teller
		je les_fra_stack
		popl %edx
		cmpl $10, %edx
		jae legg_til_hex_bokstav
		addl $48, %edx
		movb %dl, (%ecx)
		incl %ecx
		subl $1, teller
		jmp legg_til_hex_tall

	legg_til_hex_bokstav:
		addl $87, %edx
		movb %dl, (%ecx)
		incl %ecx
		decl teller
		jmp legg_til_hex_tall

	
	octal_met:
		
		incl %edx
		pushl %edx
		pushl %eax
		pushl %esi
		movl %ebp, %eax
		addl arg, %eax
		movl (%eax), %eax
		addl $4, arg
		movl $0, teller
		movl $8, %esi
		cmpl $0, %eax
		je ingen
		jmp octal_l
		
	octal_l:
		cmpl $0, %eax
		jz legg_til_octal_tall
		movl $0, %edx
		divl %esi
		pushl %edx
		addl $1, teller
		incl nummer
		jmp octal_l
		
	legg_til_octal_tall:
		cmpl $0, teller
		je les_fra_stack
		popl %edx
		
		addl $48, %edx
		movb %dl, (%ecx)
		incl %ecx
		subl $1, teller
		jmp legg_til_octal_tall
	
		
	desimal_met:
		incl %edx
		pushl %edx
		pushl %eax
		pushl %esi
		movl %ebp, %eax
		addl arg, %eax
		movl (%eax), %eax
		addl $4, arg
		movl $0, nummer
		movl $0, teller
		movl $10, %esi
		cmpl $0, %eax
		je ingen
		cmpl $0, %eax
		js minus
		jmp lokke

	lokke:
		cmpl $0, %eax
		jz fjern
		movl $0, %edx
		idivl %esi
		pushl %edx
		incl teller
		incl nummer
		jmp lokke

	fjern:
		cmpl $0, ant_plass
		jbe legg_til_desimal
		cmpl $0, nummer
		jbe legg_til_desimal 
		jz legg_til_desimal
		decl %ecx
		decl nummer
		jmp fjern

	minus:
		neg %eax
		incl negativ
		incl nummer
		jmp lokke

	kalkuler:
		movl $0, legg_til
		cmpb tegn, %al
		je legg_til_tegn
		cmpb %al, desimal
		je legg_desimal
		jmp tmp

	legg_til_tegn:
		cmpl $0, nummer
		je les
		movb mellom, %al
		movb %al, (%ecx)
		incl %ecx
		incl ant_plass
		subl $1, nummer
		jmp legg_til_tegn

	legg_desimal:
		cmpl $0, nummer
		jbe les
		movb $32, (%ecx)
		incl %ecx
		subl $1, nummer
		jmp legg_desimal

	les_fra_stack:
		popl %esi
		popl %eax
		popl %edx
		movl $0, ant_plass
		movl $0, nummer
		jmp strcpy
	#
	legg_til_desimal:
		cmpl $1, negativ
		je flipp
		cmpl $0, teller
		je les_fra_stack
		popl %edx
		addl $48, %edx
		movb %dl, (%ecx)
		incl %ecx
		subl $1, teller
		jmp legg_til_desimal


	#skriver på stacken
	skriv:
		pushl %eax
		movl $0, %edi
		movl $0, teller
		jmp atoi
	#setter minus
	flipp:
		movb $45, (%ecx)
		incl %ecx
		decl negativ
		jmp legg_til_desimal
	#samme som les bare en mer midlertidlig bruk
	tmp:
		popl %eax
		jmp samm_tegn
	#leser fra stacken
	les:
		popl %eax
		jmp samm_tegn
	#avslutter programmet
	exit:
		movl %ebp, %esp
		popl %ebp
		ret
	
