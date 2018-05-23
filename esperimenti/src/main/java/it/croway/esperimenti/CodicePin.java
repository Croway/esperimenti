package it.croway.esperimenti;

public class  CodicePin {
    public static void main(String[] args) {
        try {
           
            args= new String[]{"4062944"};
           
           
           
            for (int i =0 ;i<args.length;i++)
                System.out.println("Ditta " + args[i] +" pin: "+ getCodicePin(args[i])+ " rag sociale: "+ "ALPI 2017 ");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   
    private static int listaValori[] = {0,7,1,3,5,7,1,3,5};
   
    public     CodicePin(){
        super();
    }
   
    public static String getCodicePin(String codCli) throws Exception{
        int x = codCli.length()+1;
                int num = 0;
                int somma = 0;
                String cod1 = "";
                String cod2 = "";
                for (int i =(9-codCli.length()); i <= 8; i++ ){
                    x--;
                    num = new Integer(codCli.substring(codCli.length()-x,(codCli.length()-x)+1)).intValue();
                    somma += (num * listaValori[i]);
                }
                num = 1000-somma;
                cod1 = Integer.toString(num).substring(1,3);
       
                x = num % 101;

                if (x>99)
                    cod2 = "10";
                else
                    cod2 = Integer.toString(x);

                if (x<10)
                    cod2 = "0"+ Integer.toString(x);
           
           
       return cod1+cod2;
    }
   
   
   
}