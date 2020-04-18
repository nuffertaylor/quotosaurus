#include <stdio.h>
#include <stdlib.h>
#include <sstream> 
#include <iostream>
#include <fstream>
#include "parser.h"

using namespace std;

int main (int argc, char *argv[])
{
    ifstream inputFile;
    inputFile.open(argv[1]);
    ostream& outputFile = (argc > 2) ? *(new ofstream(argv[2])) : cout;
    Parser parser(inputFile);
    
    inputFile.close();
    if (&outputFile != &cout) delete (&outputFile);
    return 0;
}