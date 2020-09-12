/*-------------------------------------
defines.h
-------------------------------------*/

#include <nds.h>
#include <stdio.h>

// Aqui se definen los registros del gestor de interrupciones
#define IME		(*(vuint16*)0x04000208) //Interrupt Master Enable - Habilita o deshabilita todas las interrupciones
#define IE		(*(vuint32*)0x04000210) //Interrupt Enable - Activa o desactiva las interrupciones una a una
#define IF		(*(vuint32*)0x04000214) //Interrupt Flag - Guarda las peticiones de interrupcion

// Aqui se definen algunas funciones para el tratamiento de las interrupciones
#define EnableInts() 	IME=1 //Habilita todas las interrupciones
#define DisableInts() 	IME=0 //Deshabilita todas las interrupciones

// Aqui se definen los registros del teclado 
#define TECLAS_DAT	(*(vuint16*)0x4000130) //Registro de datos del teclado
#define TECLAS_CNT	(*(vuint16*)0x4000132) //Registro de control del teclado

// Aqui se definen los registros del temporizador
#define TIMER0_CNT   	(*(vuint16*)0x04000102) //Registro de control del temporizador
// El temporizador se activa poniendo un 1 en el bit 7.
// El temporizador interrumpira al desbordarse el contador, si hay un 1 en el bit 6.
// Los dos bits de menos peso indican lo siguiente:
//		00 frecuencia 33554432 hz
//		01 frecuencia 33554432/64 hz
//		10 frecuencia 33554432/256 hz
//		11 frecuencia 33554432/1024 hz

#define TIMER0_DAT    (*(vuint16*)0x04000100) //Registro de datos del temporizador
// Se utiliza para indicar a partir de que valor tiene que empezar a contar

// Esta funcion consulta si se ha tocado la pantalla tactil
extern int TactilTocada();

// Para no liarse con los numeros a cada teclas se le ha asignado un nombre
#define A		0 
#define B		1
#define SELECT	2 
#define START	3
#define DCHA	4 
#define IZDA	5
#define ARRIBA	6 
#define ABAJO	7
#define R		8 
#define L		9

// Asignamos a las direcciones valores integer
#define DIR_RIGHT	0
#define DIR_UP		1
#define DIR_LEFT	2
#define DIR_DOWN	3

// Asignar un nombre a cada estado
#define ST_OFF		0
#define ST_WAIT		1
#define ST_RUN		2
#define ST_MOVE		3
#define ST_PAUSE	4
#define ST_STOP		5

// Asignar nombre a dificultad
#define FACIL		0
#define INTERMEDIO	1
#define DIFICIL		2

// Valor maximo de los numeros random (pantalla vertical y pantalla horizontal) (distancia de la pantalla -16)
#define PANT_V	176
#define PANT_H	240

// Motivo de partida finalizada
#define GO_TIME		0
#define GO_CAPTURA	1
#define GO_TECLA	2

// En partida
#define TIEMPO_PARTIDA	180
#define TICK_SEGUNDO	512
#define NSALTOS			3

// Frecuencias de movimiento sprites
#define FREC_PACMAN		14
#define FREC_FAN1		10
#define FREC_FAN2		8
#define FREC_FAN3		12

// Numero de pixels que saltan los sprites
#define PX_PACMAN	40
#define PX_FAN		25

// Distancia a la que consideraremos 2 sprites superpuestos
#define DISTANCIA_CAPTURA	5

// Tiempo de saltos de fantasmas
#define TIEMPO_SALTO1	5
#define TIEMPO_SALTO2	3

// Oam index
#define OAM_PUNTO1	123
#define OAM_PUNTO2	122
#define OAM_PUNTO3	121
#define OAM_PUNTO4	120
#define OAM_PUNTO5	119
#define OAM_CEREZA1	118
#define OAM_CEREZA2	117
#define OAM_MIERDA1	116
#define OAM_MIERDA2 115

// Variables globales
extern int estado;
extern int dificultad;
extern int score;
extern int seg;
extern int motivoGO;
extern int ticks;
extern int tickMove1;
extern int tickMove2;
extern int tickMove3;
extern int tickCD;
extern int delaySalto;
extern int nSaltos;
