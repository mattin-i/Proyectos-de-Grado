/*-------------------------------------
 sprites.c
-------------------------------------*/
/*---------------------------------------------------------------------------------
Este codigo se ha implementado basandose en el ejemplo "Simple sprite demo" de 
dovoto y otro de Jaeden Amero
---------------------------------------------------------------------------------*/

#include <nds.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "sprites.h"
#include "defines.h"

u16* gfxMsPacman;
u16* gfxPacdot;
u16* gfxFantasma;
u16* gfxFantasma2;
u16* gfxFantasma3;
u16* gfxCereza;
u16* gfxMierda;


/* Inicializar la memoria de Sprites. */
void initSpriteMem() {

	//int i;
	oamInit(&oamMain, SpriteMapping_1D_32, false);
	oamInit(&oamSub, SpriteMapping_1D_32, false);

	gfxMsPacman = oamAllocateGfx(&oamMain, SpriteSize_16x16, SpriteColorFormat_256Color);
	gfxPacdot   = oamAllocateGfx(&oamMain, SpriteSize_16x16, SpriteColorFormat_256Color);
	gfxFantasma = oamAllocateGfx(&oamMain, SpriteSize_16x16, SpriteColorFormat_256Color);
	gfxFantasma2 = oamAllocateGfx(&oamMain, SpriteSize_16x16, SpriteColorFormat_256Color);
	gfxFantasma3 = oamAllocateGfx(&oamMain, SpriteSize_16x16, SpriteColorFormat_256Color);
	gfxCereza = oamAllocateGfx(&oamMain, SpriteSize_16x16, SpriteColorFormat_256Color);
	gfxMierda = oamAllocateGfx(&oamMain, SpriteSize_16x16, SpriteColorFormat_256Color);
}


/* Dentro de esta función hay que definir el color con el que se mostrará cada uno de los 256 
 * colores posibles en la pantalla principal. El 0 es transparente y los no definidos son negros.
 */
void establecerPaletaPrincipal() {
                                         // 0: TRANSPARENTE
   SPRITE_PALETTE[1]  = RGB15(31,0,0);   // ROJO:           RGB24={FF,00,00}
   SPRITE_PALETTE[2]  = RGB15(31,31,0);  // AMARILLO:       RGB24={FF,FF,00}
   SPRITE_PALETTE[3]  = RGB15(31,31,31); // BLANCO:         RGB24={FF,FF,FF}
   SPRITE_PALETTE[4]  = RGB15(0,31,0);   // VERDE:          RGB24={00,FF,00}
   SPRITE_PALETTE[5]  = RGB15(0,0,31);   // AZUL:           RGB24={00,00,FF}
   SPRITE_PALETTE[6]  = RGB15(0,0,0);    // NEGRO:          RGB24={00,00,00}
   SPRITE_PALETTE[7]  = RGB15(0,31,31);  // CYAN:           RGB24={00,FF,FF}
   SPRITE_PALETTE[8]  = RGB15(31,0,31);  // MAGENTA:        RGB24={FF,00,FF}
   SPRITE_PALETTE[9]  = RGB15(16,16,16); // GRIS:           RGB24={80,80,80}
   SPRITE_PALETTE[10] = RGB15(25,25,25); // GRIS CLARO:     RGB24={D0,D0,D0}
   SPRITE_PALETTE[11] = RGB15(8,8,8);    // GRIS OSCURO:    RGB24={40,40,40}
   SPRITE_PALETTE[12] = RGB15(31,19,0);  // NARANJA:        RGB24={FF,99,00}
   SPRITE_PALETTE[13] = RGB15(19,0,4);   // GRANATE:        RGB24={99,00,21}
   SPRITE_PALETTE[14] = RGB15(25,0,0);   // MARRON:         RGB24={66,00,00}
   SPRITE_PALETTE[15] = RGB15(16,0,16);  // MORADO:         RGB24={80,00,80}
   SPRITE_PALETTE[16] = RGB15(25,19,31); // LILA:           RGB24={CC,99,FF}
   SPRITE_PALETTE[17] = RGB15(31,19,25); // ROSA:           RGB24={FF,99,CC}
   SPRITE_PALETTE[18] = RGB15(23,21,21); // AZUL CLARO:     RGB24={BB,FF,FF}
   SPRITE_PALETTE[19] = RGB15(0,0,16);   // AZUL MARINO:    RGB24={00,00,80}
   SPRITE_PALETTE[20] = RGB15(0,16,16);  // VERDE AZULADO:  RGB24={00,80,80}
   SPRITE_PALETTE[21] = RGB15(0,12,0);   // VERDE OSCURO:   RGB24={00,66,00}
   SPRITE_PALETTE[22] = RGB15(16,16,0);  // VERDE OLIVA:    RGB24={80,80,00}
   SPRITE_PALETTE[23] = RGB15(19,31,19); // VERDE CLARO:    RGB24={99,FF,99}
   SPRITE_PALETTE[24] = RGB15(31,31,19); // AMARILLO CLARO: RGB24={FF,FF,99}
}

/* Dentro de esta función hay que definir el color con el que se mostrará cada uno de los 256 
 * colores posibles en la pantalla secundaria. El 0 es transparente y los no definidos son negros.
 */
void establecerPaletaSecundaria() {
   SPRITE_PALETTE_SUB[1] = RGB15(0,31,0);   // los pixels a 1 se mostrarán verdes
   SPRITE_PALETTE_SUB[2] = RGB15(31,31,31); // los pixels a 1 se mostrarán blancos
   SPRITE_PALETTE_SUB[3] = RGB15(31,31,0);  // los pixels a 1 se mostrarán amarillos
}


/* Dibujado de un Sprite de 16x16 pixels */
/* Debido al funcionamiento de los bancos de memoria, las primeras cuatro filas 
 * forman el cuadrante superior izquiero, las siguientes, el cuadrante superior 
 * derecho, las siguientes el cuadrante inferior izquierdo y las últimas cuatro
 * filas, el cuadrante inferior derecho, como se muestra al lado.
 */

u8 Fantasma[256] = 
{
	0,0,0,0,0,0,6,6,0,0,0,0,6,6,1,1, // 0000006666000000
	0,0,0,6,1,1,1,1,0,0,6,1,1,1,1,1, // 0000661111660000
	0,6,1,1,1,3,3,1,0,6,1,1,3,3,3,3, // 0006111111116000
	0,6,1,1,3,3,5,5,6,1,1,1,3,3,5,5, // 0061111111111600

	6,6,0,0,0,0,0,0,1,1,6,6,0,0,0,0, // 0611133111133160
	1,1,1,1,6,0,0,0,1,1,1,1,1,6,0,0, // 0611333311333360
	1,1,1,3,3,1,6,0,1,1,3,3,3,3,6,0, // 0611335511335560
	1,1,3,3,5,5,6,0,1,1,3,3,5,5,1,6, // 6111335511335516

	6,1,1,1,1,3,3,1,6,1,1,1,1,1,1,1, // 6111133111133116
	6,1,1,1,1,1,1,1,6,1,1,1,1,1,1,1, // 6111111111111116
	6,1,1,1,1,1,1,1,6,1,1,6,1,1,1,6, // 6111111111111116
	6,1,6,0,6,1,1,6,0,6,0,0,0,6,6,0, // 6111111111111116

	1,1,1,3,3,1,1,6,1,1,1,1,1,1,1,6, // 6111111111111116
	1,1,1,1,1,1,1,6,1,1,1,1,1,1,1,6, // 6116111661116116
	1,1,1,1,1,1,1,6,6,1,1,1,6,1,1,6, // 6160611661160616
	6,1,1,6,0,6,1,6,0,6,6,0,0,0,6,0, // 0600066006600060
};

u8 MsPacman[256]=
{
	0,0,0,0,6,6,0,0,0,0,0,6,1,1,6,6, // 0000660000000000
	0,0,0,6,1,1,1,2,0,6,6,1,5,1,1,2, // 0006116666660000
	6,1,1,5,1,2,2,2,6,1,1,1,2,2,2,2, // 0006111222226600
	0,6,1,1,2,2,2,2,0,6,2,2,2,2,2,2, // 0661511222222260

	0,0,0,0,0,0,0,0,6,6,6,6,0,0,0,0, // 6115122222222226
	2,2,2,2,6,6,0,0,2,2,2,2,2,2,6,0, // 6111222266222116
	2,2,2,2,2,2,2,6,6,6,2,2,2,1,1,6, // 0611222252222660
	5,2,2,2,2,6,6,0,2,2,6,6,6,0,0,0, // 0622222222666000

	0,6,2,2,2,2,6,6,0,6,2,2,2,2,2,2, // 0622226666000000
	0,6,2,2,2,6,2,2,0,0,6,2,2,2,2,2, // 0622222222666000
	0,0,6,2,2,2,2,2,0,0,0,6,2,2,2,2, // 0622262222222660
	0,0,0,0,6,6,2,2,0,0,0,0,0,0,6,6, // 0062222222222116

	6,6,0,0,0,0,0,0,2,2,6,6,6,0,0,0, // 0062222222222226
	2,2,2,2,2,6,6,0,2,2,2,2,2,1,1,6, // 0006222222222260
	2,2,2,2,2,2,2,6,2,2,2,2,2,2,6,0, // 0000662222226600
	2,2,2,2,6,6,0,0,6,6,6,6,0,0,0,0, // 0000006666660000
};

u8 Pacdot[256] = 
{
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 0000000000000000
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 0000000000000000
	0,0,0,0,0,0,17,17,0,0,0,0,0,17,17,17,    // 0000000000000000
	0,0,0,0,17,17,17,17,0,0,0,0,17,17,17,17, // 0000000000000000

	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 00000017171717000000
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 0000017171717171700000
	17,17,0,0,0,0,0,0,17,17,17,0,0,0,0,0,    // 000017171717171717170000
	17,17,17,17,0,0,0,0,17,17,17,17,0,0,0,0, // 000017171717171717170000

	0,0,0,0,17,17,17,17,0,0,0,0,17,17,17,17, // 000017171717171717170000
	0,0,0,0,0,17,17,17,0,0,0,0,0,0,17,17,    // 000017171717171717170000
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 0000017171717171700000
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 00000017171717000000

	17,17,17,17,0,0,0,0,17,17,17,17,0,0,0,0, // 0000000000000000
	17,17,17,0,0,0,0,0,17,17,0,0,0,0,0,0,    // 0000000000000000
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 0000000000000000
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,         // 0000000000000000
};

u8 Fantasma2[256]={
	0,0,0,0,0,0,6,6,0,0,0,0,6,6,17,17,
	0,0,0,6,17,17,17,17,0,0,6,17,17,17,17,17,
	0,6,17,17,3,3,17,17,0,6,17,3,3,3,3,17,
	0,6,17,3,3,3,3,17,6,17,17,3,5,5,3,17,
	6,6,0,0,0,0,0,0,17,17,6,6,0,0,0,0,
	17,17,17,17,6,0,0,0,17,17,17,17,17,6,0,0,
	17,17,3,3,17,17,6,0,17,3,3,3,3,17,6,0,
	17,3,3,3,3,17,6,0,17,3,5,5,3,17,17,6,
	6,17,17,17,5,5,17,17,6,17,17,17,17,17,17,17,
	6,17,17,17,17,17,17,17,6,17,17,17,17,17,17,17,
	6,17,17,17,17,17,17,17,6,17,17,17,17,6,17,17,
	0,6,17,17,6,0,6,17,0,0,6,6,0,0,0,6,
	17,17,5,5,17,17,17,6,17,17,17,17,17,17,17,6,
	17,17,17,17,17,17,17,6,17,17,17,17,17,17,17,6,
	17,17,17,17,17,17,17,6,17,17,6,17,17,17,17,6,
	17,6,0,6,17,17,6,0,6,0,0,0,6,6,0,0,
};

u8 Fantasma3[256]={
	0,0,0,0,0,0,6,6,0,0,0,0,6,6,7,7,
	0,0,0,6,7,7,7,7,0,0,6,7,7,7,7,7,
	0,6,7,3,3,7,7,7,0,6,3,3,3,3,7,7,
	0,6,5,5,3,3,7,7,6,7,5,5,3,3,7,7,
	6,6,0,0,0,0,0,0,7,7,6,6,0,0,0,0,
	7,7,7,7,6,0,0,0,7,7,7,7,7,6,0,0,
	7,3,3,7,7,7,6,0,3,3,3,3,7,7,6,0,
	5,5,3,3,7,7,6,0,5,5,3,3,7,7,7,6,
	6,7,7,3,3,7,7,7,6,7,7,7,7,7,7,7,
	6,7,7,7,7,7,7,7,6,7,7,7,7,7,7,7,
	6,7,7,7,7,7,7,7,6,7,7,6,7,7,7,6,
	6,7,6,0,6,7,7,6,0,6,0,0,0,6,6,0,
	7,3,3,7,7,7,7,6,7,7,7,7,7,7,7,6,
	7,7,7,7,7,7,7,6,7,7,7,7,7,7,7,6,
	7,7,7,7,7,7,7,6,6,7,7,7,6,7,7,6,
	6,7,7,6,0,6,7,6,0,6,6,0,0,0,6,0,
};

u8 Cereza[256]={
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	0,0,0,0,0,0,0,6,0,0,6,6,6,6,6,4,
	0,6,1,1,1,1,4,6,6,1,1,1,1,4,1,1,
	0,0,0,0,0,6,6,0,0,0,0,0,6,4,4,6,
	0,0,6,6,4,4,6,0,6,6,4,4,4,6,0,0,
	4,4,6,4,6,0,0,0,6,6,0,4,6,0,0,0,
	0,0,4,6,0,0,0,0,6,4,6,6,0,0,0,0,
	6,1,1,1,1,1,1,6,6,1,3,1,1,1,6,1,
	6,1,1,3,1,1,6,1,0,6,1,1,1,1,6,1,
	0,0,6,6,6,6,6,1,0,0,0,0,0,0,0,6,
	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
	1,4,1,1,6,0,0,0,1,4,1,1,1,6,0,0,
	1,1,1,1,1,6,0,0,3,1,1,1,1,6,0,0,
	1,3,1,1,1,6,0,0,1,1,1,1,6,0,0,0,
	6,6,6,6,0,0,0,0,0,0,0,0,0,0,0,0,
};

u8 Mierda[256]={
	0,0,0,0,0,6,6,6,0,0,0,0,6,14,14,14,
	0,0,0,0,0,6,14,14,0,0,0,0,0,6,14,14,
	0,0,0,0,6,14,14,14,0,0,0,0,6,14,14,14,
	0,0,0,6,14,14,14,14,0,0,6,14,3,3,3,14,
	0,0,0,0,0,0,0,0,6,0,0,0,0,0,0,0,
	14,6,0,0,0,0,0,0,14,14,6,0,0,0,0,0,
	14,14,14,6,0,0,0,0,14,14,14,6,0,0,0,0,
	14,14,14,14,6,0,0,0,14,3,3,3,14,6,0,0,
	0,0,6,14,3,6,3,14,0,0,6,14,3,6,3,14,
	0,6,14,14,3,3,3,14,0,6,14,14,14,14,14,14,
	6,14,14,14,3,14,14,14,6,14,14,14,14,3,3,3,
	0,6,14,14,14,14,14,14,0,0,6,6,6,6,6,6,
	14,3,6,3,14,6,0,0,14,3,6,3,14,6,0,0,
	14,3,3,3,14,14,6,0,14,14,14,14,14,14,6,0,
	14,14,14,3,14,14,14,6,3,3,3,14,14,14,14,6,
	14,14,14,14,14,14,6,0,6,6,6,6,6,6,0,0,
};

void GirarPacman(int angle){
	//128 giro de 90 grados
	oamRotateScale(&oamMain, 31, (128*angle)<<6, 1<<8, 1<<8);
	oamUpdate(&oamMain);
}

void MostrarMsPacman(int x, int y){
oamSet(&oamMain, //main graphics engine context
	127,     //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	1,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxMsPacman,//+16*16/2,	//pointer to the loaded graphics
	31,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	false,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	);   
oamUpdate(&oamMain);

}

void BorrarMsPacman(int x, int y){
oamSet(&oamMain, //main graphics engine context
	127,     //oam index (0 to 127)
	x, y,    //x and y pixle location of the sprite
	1,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite
	SpriteSize_16x16,
	SpriteColorFormat_256Color,
	gfxMsPacman,//+16*16/2,	//pointer to the loaded graphics
	31,                  	//sprite rotation data
	false,               	//double the size when rotating?
	true,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	);
oamUpdate(&oamMain);
}

void MostrarFantasma (int x, int y){
oamSet(&oamMain, //main graphics engine context
	126,     //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	0,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxFantasma,//+16*16/2,	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	false,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain);  
}

void BorrarFantasma(int x, int y){
oamSet(&oamMain, //main graphics engine context
	126,     //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	0,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxFantasma,//+16*16/2,	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	true,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain); 
}

void MostrarFantasma2 (int x, int y){
oamSet(&oamMain, //main graphics engine context
	125,     //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	0,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxFantasma2,//+16*16/2,	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	false,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain);  
}

void BorrarFantasma2(int x, int y){
oamSet(&oamMain, //main graphics engine context
	125,     //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	0,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxFantasma2,//+16*16/2,	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	true,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain); 
}

void MostrarFantasma3 (int x, int y){
oamSet(&oamMain, //main graphics engine context
	124,     //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	0,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxFantasma3,//+16*16/2,	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	false,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain);  
}

void BorrarFantasma3(int x, int y){
oamSet(&oamMain, //main graphics engine context
	124,     //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	0,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxFantasma3,//+16*16/2,	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	true,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain); 
}

void MostrarPacdot (int indice, int x, int y){ 
oamSet(&oamMain, //main graphics engine context
	indice,  //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	2,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxPacdot,//+16*16/2, 	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	false,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain);  
}

void BorrarPacdot(int indice, int x, int y) {
oamSet(&oamMain, //main graphics engine context
	indice,  //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	2,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxPacdot,//+16*16/2, 	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	true,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain); 
}

void MostrarCereza (int indice, int x, int y){ 
oamSet(&oamMain, //main graphics engine context
	indice,  //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	2,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxCereza,//+16*16/2, 	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	false,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain);  
}

void BorrarCereza(int indice, int x, int y) {
oamSet(&oamMain, //main graphics engine context
	indice,  //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	2,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxCereza,//+16*16/2, 	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	true,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain); 
}

void MostrarMierda (int indice, int x, int y){ 
oamSet(&oamMain, //main graphics engine context
	indice,  //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	2,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxMierda,//+16*16/2, 	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	false,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain);  
}

void BorrarMierda(int indice, int x, int y) {
oamSet(&oamMain, //main graphics engine context
	indice,  //oam index (0 to 127)  
	x, y,    //x and y pixle location of the sprite
	2,       //priority, lower renders last (on top)
	0,	 //this is the palette index if multiple palettes or the alpha value if bmp sprite	
	SpriteSize_16x16,     
	SpriteColorFormat_256Color, 
	gfxMierda,//+16*16/2, 	//pointer to the loaded graphics
	-1,                  	//sprite rotation data  
	false,               	//double the size when rotating?
	true,			//hide the sprite?
	false, false, 		//vflip, hflip
	false			//apply mosaic
	); 
oamUpdate(&oamMain); 
}

/* Para cada Sprite que se quiera llevar a pantalla hay que hacer una de estas funciones. */

void guardarSpritesEnMemoria(){ 
  int i;
  for(i = 0; i < 16 * 16 / 2; i++){ //muestra un cuadrado en la memoria de la pantalla principal		
     gfxPacdot[i]   = Pacdot[i*2]   | (Pacdot[(i*2)+1]<<8);
     gfxFantasma[i] = Fantasma[i*2] | (Fantasma[(i*2)+1]<<8);	
     gfxMsPacman[i] = MsPacman[i*2] | (MsPacman[(i*2)+1]<<8);	
     gfxFantasma2[i] = Fantasma2[i*2] | (Fantasma2[(i*2)+1]<<8);
     gfxFantasma3[i] = Fantasma3[i*2] | (Fantasma3[(i*2)+1]<<8);
     gfxCereza[i]   = Cereza[i*2]   | (Cereza[(i*2)+1]<<8);
     gfxMierda[i]   = Mierda[i*2]   | (Mierda[(i*2)+1]<<8);
  }
}
