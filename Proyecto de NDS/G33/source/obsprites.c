/*-------------------------------------
 obsprites.c
-------------------------------------*/

#include "defines.h"
#include "obsprites.h"

struct Sprit miPacman;
struct Sprit fantasma1;
struct Sprit fantasma2;
struct Sprit fantasma3;
struct Sprit punto1;
struct Sprit punto2;
struct Sprit punto3;
struct Sprit punto4;
struct Sprit punto5;
struct Sprit cereza1;
struct Sprit cereza2;
struct Sprit mierda1;
struct Sprit mierda2;

// Inicializa sprit con valores aleatorios
void inicializarSprit(struct Sprit *sp){
	sp->xSprit = rand() % PANT_H;
	sp->ySprit = rand() % PANT_V;
}

// Mueve el sprit segun la direccion que apunte
void moverSprit(struct Sprit *pac){
	int xP = pac->xSprit;
	int yP = pac->ySprit;
	int dirP = pac->dirSprit;

	switch(dirP){
		case DIR_UP:
			if(yP>PANT_V){
				pac->ySprit = 0;
			} else if(yP<0) {
				pac->ySprit = PANT_V;
			} else {
				pac->ySprit = yP - 1;
			}
			break;
		case DIR_DOWN:
			if(yP>PANT_V){
				pac->ySprit = 0;
			} else if(yP<0) {
				pac->ySprit = PANT_V;
			} else {
				pac->ySprit = yP + 1;
			}
			break;
		case DIR_RIGHT:
			if(xP>PANT_H){
				pac->xSprit = 0;
			} else if(xP<0) {
				pac->xSprit = PANT_H;
			} else {
				pac->xSprit = xP + 1;
			}
			break;
		case DIR_LEFT:
			if(xP>PANT_H){
				pac->xSprit = 0;
			} else if(xP<0) {
				pac->xSprit = PANT_H;
			} else {
				pac->xSprit = xP - 1;
			}
			break;
		default:
			break;
	}
}

// Cambia la direccion del sprit por la indicada por parametro
void cambiarDireccion(struct Sprit *pac, int dir){
	pac->dirSprit = dir;
}

// Funcion que cambia la direccion del perseguidor
void perseguirPac(struct Sprit *fan, struct Sprit *pac){ // Primer parametro perseguidor, segundo parametro perseguido
	int disX = pac->xSprit - fan->xSprit;
	int disY = pac->ySprit - fan->ySprit;

	if(disX>0 && disY>0) {
		if(disX>disY) {
			fan->dirSprit = DIR_RIGHT;
		} else {
			fan->dirSprit = DIR_DOWN;
		}
	} else if(disX>0 && disY<0) {
		if(disX>fabs(disY)) {
			fan->dirSprit = DIR_RIGHT;
		} else {
			fan->dirSprit = DIR_UP;
		}
	} else if(disX<0 && disY>0) {
		if(fabs(disX)>disY) {
			fan->dirSprit = DIR_LEFT;
		} else {
			fan->dirSprit = DIR_DOWN;
		}
	} else {
		if(fabs(disX)>fabs(disY)) {
			fan->dirSprit = DIR_LEFT;
		} else {
			fan->dirSprit = DIR_UP;
		}
	}
}

// Funcion que devuelve true si el primer sprite se sobrepone sobre el segundo
bool capturado(struct Sprit *sp1, struct Sprit *sp2){
	if (fabs(sp2->xSprit - sp1->xSprit) < DISTANCIA_CAPTURA && fabs(sp2->ySprit - sp1->ySprit) < DISTANCIA_CAPTURA){
		return true;
	} else {
		return false;
	}
}
