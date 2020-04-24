#include <fstream>
#include <iostream>
#include <cctype>
#include <set>
#include <vector>
#include "quoteObject.h"

using namespace std;

class Parser
{
    public:
        
        Parser(ifstream &input)
        {
            startQuote(input);
            cout << "added " << quoteCount << " quotes." <<endl;
        }
        ~Parser(){}
        string getJson ()
        {
            stringstream data;
            data << "{\"data\": [";
            for (auto quote : quotes)
            {
                data << "{\"quote\": \"" << quote.getQuote() << "\",";
                data << "\"author\": \"" << quote.getAuthor() << "\",";
                data << "\"work\": \"" << quote.getWork() << "\"},";
            }
            string tempStr = data.str();
            tempStr.pop_back();
            tempStr = tempStr + "]}";
            // data.str(tempStr);
            data << "]}";
            return tempStr;
        }
    private:
        stringstream curBlock;
        int quoteCount;
        set<string> authors;
        vector<QuoteObject> quotes;
        
        bool startQuote(ifstream &input)
        {
            char curChar;
            QuoteObject curQuote;
            while (isspace(input.peek()))
            {
                input.get(curChar);
            }
            if (quotationMark(input))
            {
                curBlock.str(string());
                input.get(curChar);
                curBlock << curChar;
                stringstream quoteContents;
                while (!quotationMark(input) && !input.eof())
                {
                    input.get(curChar);
                    curBlock << curChar;
                    quoteContents << curChar;
                }
                curQuote.setQuote(quoteContents.str());
                input.get(curChar);
                curBlock << curChar;
                if (input.peek() == ' ')
                {
                    if (dash(input))
                    {
                        input.get(curChar);
                        curBlock << curChar;
                        input.get(curChar);
                        curBlock << curChar;
                        stringstream authorName;
                        while(!leftPrin(input) && !input.eof())
                        {
                            if (quotationMark(input)) return error();
                            input.get(curChar);
                            curBlock << curChar;
                            authorName << curChar;
                        }
                        if (input.eof()) return error();
                        authors.insert(authorName.str());
                        curQuote.setAuthor(authorName.str());
                        stringstream work;
                        while (!rightPrin(input) && !input.eof())
                        {
                            input.get(curChar);
                            curBlock << curChar;
                            if (curChar != '(') work << curChar;
                        }
                        if (input.eof()) return error();
                        curQuote.setWork(work.str());
                        input.get(curChar);
                        curBlock << curChar;
                        quoteCount++;
                        quotes.push_back (curQuote);
                        if (input.eof()) return true;
                        return startQuote(input);
                    }
                    else {return error();}
                }
                else {return error();}
            }
            else if (input.eof()) return true;
            else {return error();}
        }
        bool quotationMark(ifstream &input)
        {
            return input.peek() == '"';
        }
        bool dash(ifstream &input)
        {
            return input.peek() == '-' || input.peek() == ' ';
        }
        bool leftPrin(ifstream &input)
        {
            return input.peek() == '(';
        }
        bool rightPrin(ifstream &input)
        {
            return input.peek() == ')';
        }
        
        bool error()
        {
            cout << "\nERROR ON quote #" << quoteCount << "\nlast string block was:" << endl;
            cout << curBlock.str();
            return false;
        }
};