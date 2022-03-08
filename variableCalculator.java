import java.util.*;
public class calculation{
    public static void main(String args[]){
        System.out.println("input expression: separate variables with +-*/    (...)(...) supported.");
        System.out.println("Accepting expressions like 5(x+1), (x+1)(y-1), ((((x+1)-2)*3)/4)");
        System.out.println("Accepting variables like a1, a2, a11. All variables should be in lowercase.");
        Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
        sc.close();
        System.out.println(value(s));
    }

    public static String value(String s){
        //1.a simple check for validity: input should only contains numbers, letters and ().
        //2.remove white space, add "*", deal with (...)(...), num(...), letter(...), (...)num, (...)letter
        s=s.replaceAll("\\s", "");
        if(s.length()==0) return "NO EMPTY INPUT!!!";
        if(!(s.charAt(s.length()-1)>='0' && s.charAt(s.length()-1)<='9' || s.charAt(s.length()-1)>='a' && s.charAt(s.length()-1)<='z' || s.charAt(s.length()-1)==')')) return "Wrong input";
        if(!(s.charAt(0)>='0' && s.charAt(0)<='9' || s.charAt(0)>='a' && s.charAt(0)<='z' || s.charAt(0)=='(')) return "Wrong input";
        boolean inputstring=false;
        if(s.length()==2){
            if(s.charAt(0)>='a' && s.charAt(0)<='z') inputstring=true;
        }
        String currentstring="";
        List<String> allvariables=new ArrayList<>();
        if(s.charAt(0)>='a' && s.charAt(0)<='z'){
            currentstring=s.substring(0,1);
            inputstring=true;
            if(s.length()==1)allvariables.add(s);
        }
        else currentstring="";
        for(int i=1;i<s.length()-1;i++){
            char c=s.charAt(i);
            if(c=='('){
                inputstring=false;
                char temp=s.charAt(i-1);
                if(temp>='0' && temp<='9' || temp>='a' && temp<='z') {s=s.substring(0,i)+"*"+s.substring(i);i++;}
            }
            else if(c==')'){
                char temp=s.charAt(i+1);
                inputstring=false;
                if(temp>='0' && temp<='9' || temp>='a' && temp<='z') {s=s.substring(0,i+1)+"*"+s.substring(i+1);i++;}
            }
            else if(c>='a' && c<='z') {
                inputstring=true;
                currentstring+=s.substring(i,i+1);
                char temp=s.charAt(i-1);
                if(temp>='0' && temp<='9') {
                    s=s.substring(0,i)+"*"+s.substring(i);i++;

                }    
            }
            else if(c>='0' && c<='9'){
                if(inputstring)currentstring+=s.substring(i,i+1);
            }
            else if(c=='+' || c=='-' || c=='*' || c=='/'){
                inputstring=false;
            }
            else{
                return "Wrong input";
            }
            if(!inputstring){
                if(!currentstring.equals("")) {
                    if(!allvariables.contains(currentstring))allvariables.add(currentstring);
                    currentstring="";
                }
            }
        }
        if(s.length()>1 && inputstring){
            if(s.charAt(s.length()-1)==')'){
                if(!allvariables.contains(currentstring))allvariables.add(currentstring);
            }
            else {
                currentstring+=s.substring(s.length()-1);
                if(!allvariables.contains(currentstring))allvariables.add(currentstring);
            }
        }
        else if(!inputstring && s.charAt(s.length()-1)>='a' && s.charAt(s.length()-1)<='z') {
            if(!allvariables.contains(currentstring)) allvariables.add(s.substring(s.length()-1));
            if (s.length()>1 && s.charAt(s.length()-2)>='0' && s.charAt(s.length()-2)<='9') s=s.substring(0, s.length()-1)+"*"+s.substring(s.length()-1);
        }
        s=s.replaceAll("\\)\\(","\\)*\\(");

        //add parenthesis manually
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='*' || s.charAt(i)=='/'){
                int l=findfirst(s,i);
                int r=findlast(s,i);
                if(l==-1 || r==-1) return "Wrong input";
                else {
                    if (r!=s.length()){
                        s=s.substring(0,l)+"("+s.substring(l,r)+")"+s.substring(r);
                    }
                    else s=s.substring(0,l)+"("+s.substring(l,r)+")";
                    i++;
                }
            }
        }
        //System.out.println(s);
        //System.out.println(allvariables);
        //remove & push the same layer
        List<String> all=new ArrayList<>();
        
        if(makeList(s, all, 0, s.length()-1,  0, 0)){
            Map<String, Double> map=new HashMap<>(allvariables.size()+1);
            if(allvariables.size()>0) {
                System.out.println("Expression is valid. Please enter the variables:");
                Scanner sc=new Scanner(System.in);
                for(int i=0;i<allvariables.size();i++){
                    System.out.print(allvariables.get(i)+"=");
                    if(sc.hasNext()){
                        String inputvariable=sc.next();
                        double next=Double.valueOf(inputvariable);
                        map.put(allvariables.get(i), next);
                    }
                }
                sc.close();
            }
            else if(all.isEmpty())return"NO EMPTY INPUT!!!";
            Stack<Double> nums=new Stack<>();
            System.out.println(all);
            while(!all.isEmpty()){
                String k=all.remove(0);
                if(k.charAt(0)>='0' && k.charAt(0)<='9'){
                    nums.push(Double.valueOf(k));
                }
                else if(k.charAt(0)>='a' && k.charAt(0)<='z'){
                    nums.push(Double.valueOf(map.get(k)));
                }
                else{
                    switch(k){
                        case "+":
                            nums.push(nums.pop()+nums.pop());
                            break;
                        case "-":
                            nums.push(nums.pop()-nums.pop());
                            break;
                        case "*":
                            nums.push(nums.pop()*nums.pop());
                            break;
                        case "/":
                            nums.push(nums.pop()/nums.pop());
                            break;
                        default:
                            System.out.println("Yuan thinks this is not gonna happen!");
                    }
                }

            }
            return String.valueOf(nums.peek());
        }
        else return"input error";
    }
    

    public static int findfirst(String s, int index){ //return where to add'('
        if(index==0) return -1;
        char c=s.charAt(index-1);
        if (c>='a' && c<='z' || c>='0' && c<='9'){
            for(int i=index-1;i>=0;i--){
                char temp=s.charAt(i);
                if(temp=='+' || temp=='-' || temp=='*' || temp=='/') return i+1;
            }
            return 0;
        }
        else if(c==')'){
            Stack<Integer> st=new Stack<>();
            st.push(index-1);
            for(int i=index-2;i>=0;i--){
                if(s.charAt(i)=='(')st.pop();
                else if(s.charAt(i)==')')st.push(i);
                if(st.isEmpty()) return i;  //not dealing with errors like ((1)*
            }
            return -1;
        }
        else return -1;
    }
    public static int findlast(String s, int index){ //return where to add')'
        int l=s.length()-1;
        if(index==l) return -1;
        char c=s.charAt(index+1);
        if (c>='a' && c<='z' || c>='0' && c<='9'){
            for(int i=index+1;i<=l;i++){
                char temp=s.charAt(i);
                if(temp=='+' || temp=='-' || temp=='*' || temp=='/') return i;
            }
            return l+1;
        }
        else if(c=='('){
            Stack<Integer> st=new Stack<>();
            st.push(index+1);
            for(int i=index+2;i<=l;i++){
                if(s.charAt(i)==')')st.pop();
                else if(s.charAt(i)=='(')st.push(i);
                if(st.isEmpty()) return i+1;  //not dealing with errors like ((1)*
            }
            return -1;
        }
        else return -1;
    }
    
    public static boolean makeList(String s, List<String> all, int startindex, int endindex, int nownum, int nowexp){
        if(startindex>endindex) return true;
        int currentindex=endindex;
        boolean expectingvalue=true;   // ++ and ___+  and +___ should be wrong
        Stack<String> signs=new Stack<>();

        while(currentindex>=startindex){
            char c=s.charAt(currentindex);
            if(expectingvalue){
                if(c=='+' || c=='-' ||c=='*' || c=='/') return false;
                else{
                    int index=currentindex-1;
                    if (c!=')'){
                        while(index>=startindex && s.charAt(index)!='+' && s.charAt(index)!='-' && s.charAt(index)!='*' && s.charAt(index)!='/'){
                            index--;
                        }
                        all.add(s.substring(index+1,currentindex+1));
                        currentindex=index+1;
                    } 
                    // index end at startindex-1 or the next sign.
                    //+number, +letter +(...) accepted. all other return false;
                    else{
                        Stack<Integer> parenthesis=new Stack<>();
                        parenthesis.push(currentindex);
                        while(index>=startindex){
                            if(s.charAt(index)==')') parenthesis.push(index);
                            else if(s.charAt(index)=='(') parenthesis.pop();
                            if(parenthesis.isEmpty()) break;
                            index--;
                        }
                        if(index<startindex) return false; // no ( match found
                        if(makeList(s, all, index+1, currentindex-1, nownum, nowexp)) {
                            expectingvalue=false;
                            currentindex=index;
                        }
                        else return false;
                    }
                }
                expectingvalue=false;
            }
            else{ //not expecting values
                if(currentindex==startindex) return true;
                else if(c=='+' || c=='-' ||c=='*' || c=='/') {
                    signs.push(String.valueOf(c));
                    expectingvalue=true;
                }
                else return false;
            }
            currentindex--;
        }
        while(!signs.isEmpty())all.add(signs.pop());
        return true;

    }
    public static Map<String, Double> recordValues(List<String> allvariables){
        Scanner sc=new Scanner(System.in);
        //for()
        if(sc.hasNext()){

        }
        return null;
    }
//not accepting variables like a1 a2

    //0 in front of numbers
    //expand to()(), 5(a+1)
    //expand to 9a+8b
    //ab interpretation as ab or a*b

    //negative numbers?
    //numbers like 01
}