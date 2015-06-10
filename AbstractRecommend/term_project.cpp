#include <stdlib.h>
#include <stdio.h>
#include <string.h>
int main(){
	FILE *fPtr1, *fPtr2;
	char text[50], title[50], temp[50];
	int flag=0;
	
	/* Change The Title */
	strcpy(title, "./2003./0301008");
	/* * * * */
	
	fPtr1=fopen(title, "r");
	while(fgets(text, 50, fPtr1) != NULL){
		//start fprintf
		if(strcmp(text, "\\begin{abstract}\n")==0 || strcmp(strtok(text, " "), "\\begin{abstract}")==0){
			strcpy(temp, title);
			strcat(temp, ".abstract");
			fPtr2=fopen(temp, "w");
			flag=1;
		}
		if(strcmp(text, "\\begin{titlepage}\n")==0 || strcmp(strtok(text, " "), "\\begin{titlepage}")==0){
			strcpy(temp, title);
			strcat(temp, ".titlepage");
			fPtr2=fopen(temp, "w");
			flag=1;
		}
		if(strcmp(text, "\\begin{thebibliography}{99}\n")==0 || strcmp(strtok(text, " "), "\\begin{thebibliography}")==0){
			strcpy(temp, title);
			strcat(temp, ".thebibliography");
			fPtr2=fopen(temp, "w");
			flag=1;
		}
		
		//fprintf
		if(flag==1){
			fprintf(fPtr2, "%s", text);
		}
		
		//end fprintf
		if(strcmp(text, "\\end{abstract}\n")==0 || strcmp(strtok(text, " "), "\\end{abstract}")==0){
			fclose(fPtr2);
			flag=0;
		}
		if(strcmp(text, "\\end{titlepage}\n")==0 || strcmp(strtok(text, " "), "\\end{titlepage}")==0){
			fclose(fPtr2);
			flag=0;
		}
		if(strcmp(text, "\\end{thebibliography}\n")==0 || strcmp(strtok(text, " "), "\\end{thebibliography}")==0){
			fclose(fPtr2);
			flag=0;
		}
	}
	return 0;
}
