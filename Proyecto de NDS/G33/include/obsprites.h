/*-------------------------------------
obsprites.h
-------------------------------------*/

// Creamos la estructura de un objeto sprite
struct Sprit{
	int xSprit; // Coordenada x(0,256)
	int ySprit; // Coordenada y(0,192)
	int dirSprit; // Direccion del sprite (0 derecha, 1 arriba, 2 izquierda, 3 abajo)
};

// Creamos un objeto por cada sprites que usaremos
extern struct Sprit miPacman;
extern struct Sprit fantasma1;
extern struct Sprit fantasma2;
extern struct Sprit fantasma3;
extern struct Sprit punto1;
extern struct Sprit punto2;
extern struct Sprit punto3;
extern struct Sprit punto4;
extern struct Sprit punto5;
extern struct Sprit cereza1;
extern struct Sprit cereza2;
extern struct Sprit mierda1;
extern struct Sprit mierda2;

// Funciones de los objetos
extern void inicializarSprit(struct Sprit *sp);
extern void moverSprit(struct Sprit *pac);
extern void cambiarDireccion(struct Sprit *pac, int dir);
extern void perseguirPac(struct Sprit *fan, struct Sprit *pac);
extern bool capturado(struct Sprit *sp1, struct Sprit *sp2);
