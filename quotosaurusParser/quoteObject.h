#include <string>

using namespace std;

class QuoteObject
{
    public:
        QuoteObject(){}
        ~QuoteObject(){}
        void setQuote(string quote) {this->quote = quote;}
        void setAuthor(string author) {this->author = author;}
        void setWork(string work) {this->work = work;}
        string getQuote(){return quote;}
        string getAuthor(){return author;}
        string getWork(){return work;}
    private:
        string quote;
        string author;
        string work;
};