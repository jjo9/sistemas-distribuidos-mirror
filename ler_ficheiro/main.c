#include <stdio.h>
#include <stdlib.h>
#include <windows.h>

/* run this program using the console pauser or add your own getch, system("pause") or input loop */

int main(int argc, char *argv[]) {

	char input_line[256], filename[256];
	int counter;
	FILE *ioptr;
	printf("you will fell bad...\n");
	
	/* abrir ficheiro */
	ioptr = fopen("enter.txt", "r");
	if(ioptr == (FILE *) NULL)
	{
		printf("Impossível abrir %s\n",filename);
		exit(1);
	}
	counter=0;
	/* Ler do ficheiro e escrever no terminal */
	while(fgets(input_line, 256,ioptr) != (FILE *) NULL)
	{
		printf("%s", input_line);
		counter++;
	}
	fclose(ioptr);
	int a;
	system("pause");
	for(a=0;a<=99;a++)
	{
		ioptr = fopen("doom1.txt", "r");
		if(ioptr == (FILE *) NULL)
		{
			printf("Impossível abrir %s\n",filename);
			exit(1);
		}
		counter=0;
		/* Ler do ficheiro e escrever no terminal */
		while(fgets(input_line, 256,ioptr) != (FILE *) NULL)
		{
			printf("%s", input_line);
			counter++;
		}
		fclose(ioptr);
		Sleep(66);
		system("cls");
		ioptr = fopen("doom2.txt", "r");
		if(ioptr == (FILE *) NULL)
		{
			printf("Impossível abrir %s\n",filename);
			exit(1);
		}
		counter=0;
		/* Ler do ficheiro e escrever no terminal */
		while(fgets(input_line, 256,ioptr) != (FILE *) NULL)
		{
			printf("%s", input_line);
			counter++;
		}
		fclose(ioptr);
		Sleep(66);
		system("cls");
		ioptr = fopen("doom3.txt", "r");
		if(ioptr == (FILE *) NULL)
		{
			printf("Impossível abrir %s\n",filename);
			exit(1);
		}
		counter=0;
		/* Ler do ficheiro e escrever no terminal */
		while(fgets(input_line, 256,ioptr) != (FILE *) NULL)
		{
			printf("%s", input_line);
			counter++;
		}
		fclose(ioptr);
		Sleep(66);
		system("cls");
		ioptr = fopen("doom4.txt", "r");
		if(ioptr == (FILE *) NULL)
		{
			printf("Impossível abrir %s\n",filename);
			exit(1);
		}
		counter=0;
		/* Ler do ficheiro e escrever no terminal */
		while(fgets(input_line, 256,ioptr) != (FILE *) NULL)
		{
			printf("%s", input_line);
			counter++;
		}
		fclose(ioptr);
		Sleep(66);
		system("cls");
	}
	return 0;
}
