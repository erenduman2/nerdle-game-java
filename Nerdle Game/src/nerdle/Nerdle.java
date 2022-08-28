package nerdle;


import java.util.Random;
import java.awt.Color;
import java.io.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

public class Nerdle extends javax.swing.JFrame {
    int start;
    String equation, chosenEquation;
    String fLater;
    int choose=1;
    int satirSayisi=0; // denklem uzunluguna bagli degisecek
    int line;
    int panel;
    int mainline = 0;
    int control[], control2[];
    String operation;
    int timeControl=1;
    int fromContinue = 0;
    int enable; // değeri 0 ise oyunda input alamaz.

    public Nerdle() {
        initComponents();
            start = 1;
            enable = 1;
            jPanel1.setVisible(false);
            jPanel2.setVisible(false);
            jPanel3.setVisible(false);
            jPanel7.setVisible(false);
            jPanel4.setVisible(false);
            anaSayfa1.setVisible(false);
            seconds.setVisible(false);
            twoDot.setVisible(false);
            minutes.setVisible(false);
            footer.setVisible(false);
            startTimer();
            try{
                if(isEmpty("finishLater.txt")){
                    jButton1.setVisible(false);
                }
                else{
                    jButton1.setVisible(true);
                }
            }
            catch(IOException e){
                Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
            }
            try{
                if(!isEmpty("unfinished.txt")){
                    writeStatistics("unFinished");
                    writeUnfinished("");
                }
            }
            catch(IOException e){
                Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
            }
            try{
                readStatistics();
            }
            catch(IOException e){
                Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
            }
    }
    //Acılan dosyanın bos olup olmadıgını kontrol eder.
    public boolean isEmpty(String fileName)throws IOException{
        String line;
        File file = new File(fileName);
        if(!file.exists()){
            file.createNewFile();
        }
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        line = bReader.readLine();
        if(line == null){
            return true;
        }
        else{
            return false;
        }
    }
    //Istatistikleri dosyaya yazar.
    public void writeStatistics (String condition /*Clock*/) throws IOException{
       File file = new File("statistics.txt");
        String val;
        int i, lngt, count;
        String strCount;
        String text="";
        if(!file.exists()){
            file.createNewFile();
        }
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);        
        if(condition.equals("win")){
            for(i=0; i<5; i++){
                val = bReader.readLine();
                if(i == 0){
                    lngt = val.length();
                    strCount = val.substring(4); // Win:'den sonra gelen sayi
                    count = Integer.parseInt(strCount);
                    count++;
                    val = val.substring(0, 4) + count;
                }
                text = text + val + "\n";
            }
            FileWriter fWriter = new FileWriter(file, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(text);
            bWriter.close();
        }
        else if(condition.equals("lose")){
           for(i=0; i<5; i++){
                val = bReader.readLine();
                if(i == 1){
                    lngt = val.length();
                    strCount = val.substring(5);
                    count = Integer.parseInt(strCount);
                    count++;
                    val = val.substring(0, 5) + count;
                }
                text = text + val + "\n";
            }
            FileWriter fWriter = new FileWriter(file, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(text);
            bWriter.close();
        }
        else if(condition.equals("unFinished")){
           for(i=0; i<5; i++){
                val = bReader.readLine();
                if(i == 2){
                    lngt = val.length();
                    strCount = val.substring(11);
                    count = Integer.parseInt(strCount);
                    count++;
                    val = val.substring(0, 11) + count;
                }
                text = text + val + "\n";
            }
            FileWriter fWriter = new FileWriter(file, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(text);
            bWriter.close(); 
        }
        else if(condition.equals("averageLine")){
            for(i=0; i<5; i++){
                 val = bReader.readLine();
                 if(i == 3){
                    lngt = val.length();
                    strCount = val.substring(10);
                    count = Integer.parseInt(strCount);
                    count = count + mainline - 1;
                    val = val.substring(0, 10) + count;
                 }
                 text = text + val + "\n";
            }
            FileWriter fWriter = new FileWriter(file, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(text);
            bWriter.close(); 
        }
        else{
            for(i=0; i<5; i++){
                 val = bReader.readLine();
                 if(i == 4){
                     lngt = val.length();
                     strCount = val.substring(10);
                     count = Integer.parseInt(strCount);
                     count = count + Integer.parseInt(minutes.getText())*60 + Integer.parseInt(seconds.getText());//conut = dakika*60+saniye
                     val = val.substring(0, 10) + count;
                 }
                 text = text + val + "\n";
            }
            FileWriter fWriter = new FileWriter(file, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(text);
            bWriter.close();
        }
    }
    //Oyun daha sonra bitirilecekse bilgileri bilgileri dosyaya kaydeder.
    public void writeFinishLater()throws IOException{
        File file = new File("finishLater.txt");
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fWriter = new FileWriter(file, false);
        BufferedWriter bWriter = new BufferedWriter(fWriter);
        if(!fLater.equals("")){
            fLater = minutes.getText() + "\n" + seconds.getText() + "\n" + fLater;
        }
        bWriter.write(fLater);
        bWriter.close();   
    }
    //Dosyaya surekli oyun bilgilerini kaydeder, oyun eger bitirilirse dosyayı bosaltir.
    //Dosyada veri kalmissa oyun yarida birakilmistir.
    public void writeUnfinished(String s) throws IOException{
        File file = new File("unfinished.txt");
        String line;
        int i, lngt, count;
        String strCount;
        String text="";
        if(!file.exists()){
            file.createNewFile();
        }
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        line = bReader.readLine();
        
        while(line != null){
            text = text + line + "\n";
            line = bReader.readLine();
        }
        
        text = text + s + "\n";
        if(s.equals("")){
            text = "";
        }
        FileWriter fWriter = new FileWriter(file, false);
        BufferedWriter bWriter = new BufferedWriter(fWriter);
        bWriter.write(text);
        bWriter.close();          
    }
    //Devam edileceke oyun varsa ve devam ete basilmissa dosyadaki verileri oyun ekranina aktarır.
    public void readFinishLater() throws IOException{
        File file = new File("finishLater.txt");
        int isThere;
        int i, j;
        String tmp;
        if(!file.exists()){
            file.createNewFile();
        }
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        minutes.setText(bReader.readLine());
        seconds.setText(bReader.readLine());
        tmp = bReader.readLine();
        chosenEquation = bReader.readLine();      
        while(chosenEquation != null){
            for(i=0; i<satirSayisi; i++){
                write(i+1, mainline, panel, chosenEquation.substring(i, i+1));
                isThere=0;
                if(chosenEquation.substring(i, i+1).compareTo(equation.substring(i, i+1)) == 0){
                    paint(i+1, mainline, panel, Color.green);//c l p s
                    control[i] = 1; 
                    control2[i] = 1;
                }
                else{
                    for(j=0; j<satirSayisi; j++){
                        if(chosenEquation.substring(i, i+1).compareTo(equation.substring(j, j+1)) == 0){
                            if(control[j] == 0 && isThere == 0){
                                paint(i+1, mainline, panel, Color.yellow);
                                control[j] = 1;
                                isThere = 1;
                            }
                        }
                    }
                    if(isThere == 0){
                        paint(i+1, mainline, panel, Color.red);
                    }
                }
            }
            mainline++; 
            choose = 1;
            line++;
            operation = "---";
            
            for(i=0; i<satirSayisi; i++){
                control[i] = control2[i];
            }
            chosenEquation = bReader.readLine();
        }
    }
    
    //Devam et butonuna basıldıktan sonra fLater, FinishLater dosyasinda olan verilerle initialize edilir.
    public void initializeFLater() throws IOException{
        File file = new File("finishLater.txt");
        int i, j;
        String tmp;
        fLater = "";
        if(!file.exists()){
            file.createNewFile();
        }
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        tmp = bReader.readLine(); //dakika okundu
        tmp = bReader.readLine(); //saniye okundu
        tmp = bReader.readLine();
        while(tmp != null){
            fLater = fLater + tmp + "\n";
            tmp = bReader.readLine();
        }
    }
    
    public void readStatistics()throws IOException{
        File file = new File("statistics.txt");
        int isThere;
        int newFile = 0;
        String defaultStatistics = "Win:0\nLose:0\nUnfinished:0\nLineCount:0\nTimeCount:0";
        int i=0;
        int averageLine, averageTime, lineCount;
        String tmp, tmp2, text="";
        if(!file.exists()){
            file.createNewFile();
            newFile = 1; // eger yeni olusturuluyorsa baslangic yazilari olmalidir.
        }
        //Eger istatistikler yoksa 0'dan bastan olusturulur.
        if(newFile == 1){
            FileWriter fWriter = new FileWriter(file, false);
            BufferedWriter bWriter = new BufferedWriter(fWriter);
            bWriter.write(defaultStatistics);
            bWriter.close();
        }
        
        
        FileReader fReader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(fReader);
        tmp = bReader.readLine();
        int win = 0;
        while(tmp != null){
            if(i == 0){
                jLabel12.setText(tmp);
                tmp2 = tmp.substring(4);
                win = Integer.parseInt(tmp2);
            }
            if(i == 1){
                jLabel10.setText(tmp);
            }
            if(i == 2){
                jLabel16.setText(tmp);
            }
            if(i == 3){
                if(win == 0){
                    jLabel11.setText("Average Win Line:0");
                }
                else{
                    tmp = tmp.substring(10);
                    averageLine = Integer.parseInt(tmp) / win;
                    jLabel11.setText("Average Win Line:" + averageLine);//LineCount/Win
                }
            }
            if(i == 4){
                if(win == 0){
                    jLabel15.setText("Average Win Time:0dk. 0sn.");
                }
                else{
                    tmp = tmp.substring(10);
                    averageTime = Integer.parseInt(tmp) / win;
                    jLabel15.setText("Average Win Time:" + averageTime/60 + "dk. " + averageTime%60 + "sn.");//LineCount/Win
                }
            }
            tmp = bReader.readLine();
            i++;
        }
        
    }
    
    public int usAl(int x, int y){
        int i, tmp=1;
        for(i=0; i<y; i++){
            tmp *= x;
        }
        return tmp;
    }
    
    //Kullanicinin girdigi denklemin dogru olup olmadigini kontrol eder.
    public boolean isEquationTrue(String equation){
        int x=0, result=0, realResult;
        int digits[], numbers[];
        String operators="", operator1, operator2;
        digits = new int[equation.length()];
        numbers = new int[5];
        String tmp;
        int i, j=0, k=0, a=0, kontrol=0;
        for(i=0; i<equation.length(); i++){
            tmp = equation.substring(i, i+1);
            try{
                x = Integer.parseInt(tmp);
            }
            catch(NumberFormatException e){
                kontrol = 1;
            }
            finally{
                if(kontrol == 1){
                    for(j=i; j>0; j--){
                        result += digits[k]*usAl(10, j-1);
                        digits[k] = 0;
                        k++;
                    }
                    numbers[a] = result;
                    a++;
                    result = 0;
                    operators += tmp;
                    kontrol = 0;
                    k = 0;
                }
                else{
                    digits[i] = x;
                }
            }
        }
        for(j=i; j>0; j--){
            result += digits[k]*usAl(10, j-1);
            digits[k] = 0;
            k++;
        }
        numbers[a] = result;
        if(operators.length() == 1 || operators.length() == 0){
            return false;
        }
        if(operators.length() == 3){
            
            operator1 = operators.substring(0, 1);
            operator2 = operators.substring(1, 2);
            //='in sağ tarafında - varsa 
            if(operator2.equals("=")){
                realResult = numbers[0] - numbers[1];
                numbers[3] = -1*numbers[3];
                if(realResult == numbers[3]){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if(operator1.contains("+")){
                if(operator2.contains("+")){
                    realResult = numbers[0] + numbers[1] + numbers[2];
                }
                else if(operator2.contains("-")){
                    realResult = numbers[0] + numbers[1] - numbers[2];
                }
                else if(operator2.contains("*")){
                    realResult = numbers[0] + numbers[1] * numbers[2];
                }
                else{
                    if(numbers[1] % numbers[2] != 0){
                        return false;
                    }
                    else{
                        realResult = numbers[0] + numbers[1] / numbers[2];
                    }
                }
            }
            
            else if(operator1.contains("-")){
                if(operator2.contains("+")){
                    realResult = numbers[0] - numbers[1] + numbers[2];
                }
                else if(operator2.contains("-")){
                    realResult = numbers[0] - numbers[1] - numbers[2];
                }
                else if(operator2.contains("*")){
                    realResult = numbers[0] - numbers[1] * numbers[2];
                }
                else{
                    if(numbers[1] % numbers[2] != 0){
                        return false;
                    }
                    else{
                        realResult = numbers[0] - numbers[1] / numbers[2];
                    }
                }
            }
            
            else if(operator1.contains("*")){
                if(operator2.contains("+")){
                    realResult = numbers[0] * numbers[1] + numbers[2];
                }
                else if(operator2.contains("-")){
                    realResult = numbers[0] * numbers[1] - numbers[2];
                }
                else if(operator2.contains("*")){
                    realResult = numbers[0] * numbers[1] * numbers[2];
                }
                else{
                    if(numbers[1] % numbers[2] != 0){
                        return false;
                    }
                    else{
                        realResult = numbers[0] * numbers[1] / numbers[2];
                    }
                }
            }
            
            else{
                if(operator2.contains("+")){
                    if(numbers[0] % numbers[1] != 0){
                        return false;
                    }
                    else{
                        realResult = numbers[0] / numbers[1] + numbers[2];
                    }
                }
                else if(operator2.contains("-")){
                    if(numbers[0] % numbers[1] != 0){
                        return false;
                    }
                    else{
                        realResult = numbers[0] / numbers[1] - numbers[2];
                    }
                }
                else if(operator2.contains("*")){
                    if(numbers[0] % numbers[1] != 0){
                        return false;
                    }
                    else{
                        realResult = numbers[0] / numbers[1] * numbers[2];
                    }
                }
                else{
                    if(numbers[1] % numbers[2] != 0){
                        return false;
                    }
                    if(numbers[0] % numbers[1] != 0){
                        return false;
                    }
                    else{
                        realResult = numbers[0] / numbers[1] / numbers[2];
                    }
                }
            }
            if(realResult == numbers[3]){
                return true;
            }
            else{
                return false;
            }
        }
        else{
            operator1 = operators.substring(0, 1);
            if(operator1.contains("+")){
                realResult = numbers[0] + numbers[1];
            }
            else if(operator1.contains("-")){
                realResult = numbers[0] - numbers[1];
            }
            else if(operator1.contains("*")){
                realResult = numbers[0] * numbers[1];
            }
            else{
                if(numbers[0] % numbers[1] != 0){
                    return false;
                }
                else{
                    realResult = numbers[0] / numbers[1];
                }
            }
            if(realResult == numbers[2]){
                return true;
            }
            else{
                return false;
            }
        }
    }
    
    /*
    Her bir basamağa atama yapılması için
    */
    
    //Kullanicinin butonlara basarak girdigi sayi degerlerini yazar.
    public void write(int c, int l, int p, String s){
        if(enable == 0){
            return;
        }
        
        if(mainline == l && start == 0){
            if(s.compareTo("") == 0){
                if(c == 1){
                    chosenEquation = chosenEquation.substring(0, c-1) + "," + chosenEquation.substring(c);
                    operation= "first";
                }
                else if(operation.contains("---")){
                    chosenEquation = chosenEquation.substring(0, c-1) + "," + chosenEquation.substring(c);
                    choose--;
                    operation = "silme";
                }
                else{
                    if(operation.contains("silme")){ 
                        chosenEquation = chosenEquation.substring(0, c-1) + "," + chosenEquation.substring(c);
                        choose--;
                        operation = "silme";
                    }
                    else if(operation.contains("ekle")){
                        choose--;
                        c--;
                        chosenEquation = chosenEquation.substring(0, c-1) + "," + chosenEquation.substring(c);
                        operation = "silme";
                        choose--;
                    }
                    else if(operation.contains("last")){
                        chosenEquation = chosenEquation.substring(0, c-1) + "," + chosenEquation.substring(c);
                        choose--;
                        operation = "silme";
                    }
                    
                }
            }
            else{
                if(c == satirSayisi){
                    chosenEquation = chosenEquation.substring(0, c-1) + s + chosenEquation.substring(c);
                    operation = "last";
                }
                else if(operation.contains("---")){
                    chosenEquation = chosenEquation.substring(0, c-1) + s + chosenEquation.substring(c);
                    choose++;
                    operation = "ekle";
                }
                else if(operation.contains("ekle")){
                    chosenEquation = chosenEquation.substring(0, c-1) + s + chosenEquation.substring(c);
                    choose++;
                    operation = "ekle";
                }
                else if(operation.contains("silme")){
                    choose++;
                    c++;
                    chosenEquation = chosenEquation.substring(0, c-1) + s + chosenEquation.substring(c);
                    choose++;
                    operation = "ekle";
                }
                else if(operation.contains("first")){
                    chosenEquation = chosenEquation.substring(0, c-1) + s + chosenEquation.substring(c);
                    choose++;
                    operation = "ekle";
                }
            }
        }        
        
        if(p == 1){
            if(l == 1 && mainline == 1){
                if(c == 1){
                    jButton42.setText(s);
                }
                if(c == 2){
                    jButton41.setText(s);
                }
                if(c == 3){
                    jButton43.setText(s);
                }
                if(c == 4){
                    jButton38.setText(s);
                }
                if(c == 5){
                    jButton39.setText(s);
                }
                if(c == 6){
                    jButton40.setText(s);
                }
                if(c == 7){
                    jButton139.setText(s);
                }
                if(c == 8){
                    jButton149.setText(s);
                }
                if(c == 9){
                    jButton137.setText(s);
                }
            }
            if(l == 2 && mainline == 2){
                if(c == 1){
                    jButton48.setText(s);
                }
                if(c == 2){
                    jButton47.setText(s);
                }
                if(c == 3){
                    jButton49.setText(s);
                }
                if(c == 4){
                    jButton44.setText(s);
                }
                if(c == 5){
                    jButton45.setText(s);
                }
                if(c == 6){
                    jButton46.setText(s);
                }
                if(c == 7){
                    jButton138.setText(s);
                }
                if(c == 8){
                    jButton146.setText(s);
                }
                if(c == 9){
                    jButton150.setText(s);
                }
            }
            if(l == 3 && mainline == 3){
                if(c == 1){
                    jButton50.setText(s);
                }
                if(c == 2){
                    jButton51.setText(s);
                }
                if(c == 3){
                    jButton52.setText(s);
                }
                if(c == 4){
                    jButton54.setText(s);
                }
                if(c == 5){
                    jButton53.setText(s);
                }
                if(c == 6){
                    jButton55.setText(s);
                }
                if(c == 7){
                    jButton141.setText(s);
                }
                if(c == 8){
                    jButton140.setText(s);
                }
                if(c == 9){
                    jButton152.setText(s);
                }
            }
            if(l == 4 && mainline == 4){
                if(c == 1){
                    jButton61.setText(s);
                }
                if(c == 2){
                    jButton60.setText(s);
                }
                if(c == 3){
                    jButton59.setText(s);
                }
                if(c == 4){
                    jButton58.setText(s);
                }
                if(c == 5){
                    jButton57.setText(s);
                }
                if(c == 6){
                    jButton56.setText(s);
                }
                if(c == 7){
                    jButton142.setText(s);
                }
                if(c == 8){
                    jButton147.setText(s);
                }
                if(c == 9){
                    jButton151.setText(s);
                }
            }
            if(l == 5 && mainline == 5){
                if(c == 1){
                    jButton67.setText(s);
                }
                if(c == 2){
                    jButton66.setText(s);
                }
                if(c == 3){
                    jButton65.setText(s);
                }
                if(c == 4){
                    jButton64.setText(s);
                }
                if(c == 5){
                    jButton63.setText(s);
                }
                if(c == 6){
                    jButton62.setText(s);
                }
                if(c == 7){
                    jButton144.setText(s);
                }
                if(c == 8){
                    jButton143.setText(s);
                }
                if(c == 9){
                    jButton154.setText(s);
                }
            }
            if(l == 6 && mainline == 6){
                if(c == 1){
                    jButton73.setText(s);
                }
                if(c == 2){
                    jButton72.setText(s);
                }
                if(c == 3){
                    jButton71.setText(s);
                }
                if(c == 4){
                    jButton70.setText(s);
                }
                if(c == 5){
                    jButton69.setText(s);
                }
                if(c == 6){
                    jButton68.setText(s);
                }
                if(c == 7){
                    jButton145.setText(s);
                }
                if(c == 8){
                    jButton148.setText(s);
                }
                if(c == 9){
                    jButton153.setText(s);
                }
            } 
        }
        if(p == 2){
            if(l == 1 && mainline == 1){
                if(c == 1){
                    jButton90.setText(s);
                }
                if(c == 2){
                    jButton89.setText(s);
                }
                if(c == 3){
                    jButton91.setText(s);
                }
                if(c == 4){
                    jButton86.setText(s);
                }
                if(c == 5){
                    jButton87.setText(s);
                }
                if(c == 6){
                    jButton88.setText(s);
                }
                if(c == 7){
                    jButton122.setText(s);
                }
            }
            if(l == 2 && mainline == 2){
                if(c == 1){
                    jButton96.setText(s);
                }
                if(c == 2){
                    jButton95.setText(s);
                }
                if(c == 3){
                    jButton97.setText(s);
                }
                if(c == 4){
                    jButton92.setText(s);
                }
                if(c == 5){
                    jButton93.setText(s);
                }
                if(c == 6){
                    jButton94.setText(s);
                }
                if(c == 7){
                    jButton124.setText(s);
                }
            }
            if(l == 3 && mainline == 3){
                if(c == 1){
                    jButton98.setText(s);
                }
                if(c == 2){
                    jButton99.setText(s);
                }
                if(c == 3){
                    jButton100.setText(s);
                }
                if(c == 4){
                    jButton102.setText(s);
                }
                if(c == 5){
                    jButton101.setText(s);
                }
                if(c == 6){
                    jButton103.setText(s);
                }
                if(c == 7){
                    jButton126.setText(s);
                }
            }
            if(l == 4 && mainline == 4){
                if(c == 1){
                    jButton109.setText(s);
                }
                if(c == 2){
                    jButton108.setText(s);
                }
                if(c == 3){
                    jButton107.setText(s);
                }
                if(c == 4){
                    jButton106.setText(s);
                }
                if(c == 5){
                    jButton105.setText(s);
                }
                if(c == 6){
                    jButton104.setText(s);
                }
                if(c == 7){
                    jButton128.setText(s);
                }
            }
            if(l == 5 && mainline == 5){
                if(c == 1){
                    jButton115.setText(s);
                }
                if(c == 2){
                    jButton114.setText(s);
                }
                if(c == 3){
                    jButton113.setText(s);
                }
                if(c == 4){
                    jButton112.setText(s);
                }
                if(c == 5){
                    jButton111.setText(s);
                }
                if(c == 6){
                    jButton110.setText(s);
                }
                if(c == 7){
                    jButton130.setText(s);
                }
            }
            if(l == 6 && mainline == 6){
                if(c == 1){
                    jButton121.setText(s);
                }
                if(c == 2){
                    jButton120.setText(s);
                }
                if(c == 3){
                    jButton119.setText(s);
                }
                if(c == 4){
                    jButton118.setText(s);
                }
                if(c == 5){
                    jButton117.setText(s);
                }
                if(c == 6){
                    jButton116.setText(s);
                }
                if(c == 7){
                    jButton132.setText(s);
                }
            } 
        }
        if(p == 3){
            if(l == 1 && mainline == 1){
                if(c == 1){
                    jButton5.setText(s);
                }
                if(c == 2){
                    jButton4.setText(s);
                }
                if(c == 3){
                    jButton6.setText(s);
                }
                if(c == 4){
                    jButton7.setText(s);
                }
                if(c == 5){
                    jButton2.setText(s);
                }
                if(c == 6){
                    jButton3.setText(s);
                }
                if(c == 7){
                    jButton74.setText(s);
                }
                if(c == 8){
                    jButton75.setText(s);
                }
            }
            if(l == 2 && mainline == 2){
                if(c == 1){
                    jButton12.setText(s);
                }
                if(c == 2){
                    jButton11.setText(s);
                }
                if(c == 3){
                    jButton13.setText(s);
                }
                if(c == 4){
                    jButton8.setText(s);
                }
                if(c == 5){
                    jButton9.setText(s);
                }
                if(c == 6){
                    jButton10.setText(s);
                }
                if(c == 7){
                    jButton76.setText(s);
                }
                if(c == 8){
                    jButton77.setText(s);
                }
            }
            if(l == 3 && mainline == 3){
                if(c == 1){
                    jButton19.setText(s);
                }
                if(c == 2){
                    jButton18.setText(s);
                }
                if(c == 3){
                    jButton17.setText(s);
                }
                if(c == 4){
                    jButton16.setText(s);
                }
                if(c == 5){
                    jButton15.setText(s);
                }
                if(c == 6){
                    jButton14.setText(s);
                }
                if(c == 7){
                    jButton78.setText(s);
                }
                if(c == 8){
                    jButton79.setText(s);
                }
            }
            if(l == 4 && mainline == 4){
                if(c == 1){
                    jButton25.setText(s);
                }
                if(c == 2){
                    jButton24.setText(s);
                }
                if(c == 3){
                    jButton23.setText(s);
                }
                if(c == 4){
                    jButton22.setText(s);
                }
                if(c == 5){
                    jButton21.setText(s);
                }
                if(c == 6){
                    jButton20.setText(s);
                }
                if(c == 7){
                    jButton80.setText(s);
                }
                if(c == 8){
                    jButton81.setText(s);
                }
            }
            if(l == 5 && mainline == 5){
                if(c == 1){
                    jButton31.setText(s);
                }
                if(c == 2){
                    jButton30.setText(s);
                }
                if(c == 3){
                    jButton29.setText(s);
                }
                if(c == 4){
                    jButton28.setText(s);
                }
                if(c == 5){
                    jButton27.setText(s);
                }
                if(c == 6){
                    jButton26.setText(s);
                }
                if(c == 7){
                    jButton82.setText(s);
                }
                if(c == 8){
                    jButton83.setText(s);
                }
            }
            if(l == 6 && mainline == 6){
                if(c == 1){
                    jButton37.setText(s);
                }
                if(c == 2){
                    jButton36.setText(s);
                }
                if(c == 3){
                    jButton35.setText(s);
                }
                if(c == 4){
                    jButton34.setText(s);
                }
                if(c == 5){
                    jButton33.setText(s);
                }
                if(c == 6){
                    jButton32.setText(s);
                }
                if(c == 7){
                    jButton84.setText(s);
                }
                if(c == 8){
                    jButton85.setText(s);
                }
            } 
        }
    }
    
    //Denklemin belli kisimlarini sari, yesil ve kirmiziya boyar.
    public void paint(int c, int l, int p, Color s){
        if(p == 1){
            if(l == 1 && mainline == 1){
                if(c == 1){
                    jButton42.setBackground(s);
                }
                if(c == 2){
                    jButton41.setBackground(s);
                }
                if(c == 3){
                    jButton43.setBackground(s);
                }
                if(c == 4){
                    jButton38.setBackground(s);
                }
                if(c == 5){
                    jButton39.setBackground(s);
                }
                if(c == 6){
                    jButton40.setBackground(s);
                }
                if(c == 7){
                    jButton139.setBackground(s);
                }
                if(c == 8){
                    jButton149.setBackground(s);
                }
                if(c == 9){
                    jButton137.setBackground(s);
                }
            }
            if(l == 2 && mainline == 2){
                if(c == 1){
                    jButton48.setBackground(s);
                }
                if(c == 2){
                    jButton47.setBackground(s);
                }
                if(c == 3){
                    jButton49.setBackground(s);
                }
                if(c == 4){
                    jButton44.setBackground(s);
                }
                if(c == 5){
                    jButton45.setBackground(s);
                }
                if(c == 6){
                    jButton46.setBackground(s);
                }
                if(c == 7){
                    jButton138.setBackground(s);
                }
                if(c == 8){
                    jButton146.setBackground(s);
                }
                if(c == 9){
                    jButton150.setBackground(s);
                }
            }
            if(l == 3 && mainline == 3){
                if(c == 1){
                    jButton50.setBackground(s);
                }
                if(c == 2){
                    jButton51.setBackground(s);
                }
                if(c == 3){
                    jButton52.setBackground(s);
                }
                if(c == 4){
                    jButton54.setBackground(s);
                }
                if(c == 5){
                    jButton53.setBackground(s);
                }
                if(c == 6){
                    jButton55.setBackground(s);
                }
                if(c == 7){
                    jButton141.setBackground(s);
                }
                if(c == 8){
                    jButton140.setBackground(s);
                }
                if(c == 9){
                    jButton152.setBackground(s);
                }
            }
            if(l == 4 && mainline == 4){
                if(c == 1){
                    jButton61.setBackground(s);
                }
                if(c == 2){
                    jButton60.setBackground(s);
                }
                if(c == 3){
                    jButton59.setBackground(s);
                }
                if(c == 4){
                    jButton58.setBackground(s);
                }
                if(c == 5){
                    jButton57.setBackground(s);
                }
                if(c == 6){
                    jButton56.setBackground(s);
                }
                if(c == 7){
                    jButton142.setBackground(s);
                }
                if(c == 8){
                    jButton147.setBackground(s);
                }
                if(c == 9){
                    jButton151.setBackground(s);
                }
            }
            if(l == 5 && mainline == 5){
                if(c == 1){
                    jButton67.setBackground(s);
                }
                if(c == 2){
                    jButton66.setBackground(s);
                }
                if(c == 3){
                    jButton65.setBackground(s);
                }
                if(c == 4){
                    jButton64.setBackground(s);
                }
                if(c == 5){
                    jButton63.setBackground(s);
                }
                if(c == 6){
                    jButton62.setBackground(s);
                }
                if(c == 7){
                    jButton144.setBackground(s);
                }
                if(c == 8){
                    jButton143.setBackground(s);
                }
                if(c == 9){
                    jButton154.setBackground(s);
                }
            }
            if(l == 6 && mainline == 6){
                if(c == 1){
                    jButton73.setBackground(s);
                }
                if(c == 2){
                    jButton72.setBackground(s);
                }
                if(c == 3){
                    jButton71.setBackground(s);
                }
                if(c == 4){
                    jButton70.setBackground(s);
                }
                if(c == 5){
                    jButton69.setBackground(s);
                }
                if(c == 6){
                    jButton68.setBackground(s);
                }
                if(c == 7){
                    jButton145.setBackground(s);
                }
                if(c == 8){
                    jButton148.setBackground(s);
                }
                if(c == 9){
                    jButton153.setBackground(s);
                }
            } 
        }
        if(p == 2){
            if(l == 1 && mainline == 1){
                if(c == 1){
                    jButton90.setBackground(s);
                }
                if(c == 2){
                    jButton89.setBackground(s);
                }
                if(c == 3){
                    jButton91.setBackground(s);
                }
                if(c == 4){
                    jButton86.setBackground(s);
                }
                if(c == 5){
                    jButton87.setBackground(s);
                }
                if(c == 6){
                    jButton88.setBackground(s);
                }
                if(c == 7){
                    jButton122.setBackground(s);
                }
            }
            if(l == 2 && mainline == 2){
                if(c == 1){
                    jButton96.setBackground(s);
                }
                if(c == 2){
                    jButton95.setBackground(s);
                }
                if(c == 3){
                    jButton97.setBackground(s);
                }
                if(c == 4){
                    jButton92.setBackground(s);
                }
                if(c == 5){
                    jButton93.setBackground(s);
                }
                if(c == 6){
                    jButton94.setBackground(s);
                }
                if(c == 7){
                    jButton124.setBackground(s);
                }
            }
            if(l == 3 && mainline == 3){
                if(c == 1){
                    jButton98.setBackground(s);
                }
                if(c == 2){
                    jButton99.setBackground(s);
                }
                if(c == 3){
                    jButton100.setBackground(s);
                }
                if(c == 4){
                    jButton102.setBackground(s);
                }
                if(c == 5){
                    jButton101.setBackground(s);
                }
                if(c == 6){
                    jButton103.setBackground(s);
                }
                if(c == 7){
                    jButton126.setBackground(s);
                }
            }
            if(l == 4 && mainline == 4){
                if(c == 1){
                    jButton109.setBackground(s);
                }
                if(c == 2){
                    jButton108.setBackground(s);
                }
                if(c == 3){
                    jButton107.setBackground(s);
                }
                if(c == 4){
                    jButton106.setBackground(s);
                }
                if(c == 5){
                    jButton105.setBackground(s);
                }
                if(c == 6){
                    jButton104.setBackground(s);
                }
                if(c == 7){
                    jButton128.setBackground(s);
                }
            }
            if(l == 5 && mainline == 5){
                if(c == 1){
                    jButton115.setBackground(s);
                }
                if(c == 2){
                    jButton114.setBackground(s);
                }
                if(c == 3){
                    jButton113.setBackground(s);
                }
                if(c == 4){
                    jButton112.setBackground(s);
                }
                if(c == 5){
                    jButton111.setBackground(s);
                }
                if(c == 6){
                    jButton110.setBackground(s);
                }
                if(c == 7){
                    jButton130.setBackground(s);
                }
            }
            if(l == 6 && mainline == 6){
                if(c == 1){
                    jButton121.setBackground(s);
                }
                if(c == 2){
                    jButton120.setBackground(s);
                }
                if(c == 3){
                    jButton119.setBackground(s);
                }
                if(c == 4){
                    jButton118.setBackground(s);
                }
                if(c == 5){
                    jButton117.setBackground(s);
                }
                if(c == 6){
                    jButton116.setBackground(s);
                }
                if(c == 7){
                    jButton132.setBackground(s);
                }
            } 
        }
        if(p == 3){
            if(l == 1 && mainline == 1){
                if(c == 1){
                    jButton5.setBackground(s);
                }
                if(c == 2){
                    jButton4.setBackground(s);
                }
                if(c == 3){
                    jButton6.setBackground(s);
                }
                if(c == 4){
                    jButton7.setBackground(s);
                }
                if(c == 5){
                    jButton2.setBackground(s);
                }
                if(c == 6){
                    jButton3.setBackground(s);
                }
                if(c == 7){
                    jButton74.setBackground(s);
                }
                if(c == 8){
                    jButton75.setBackground(s);
                }
            }
            if(l == 2 && mainline == 2){
                if(c == 1){
                    jButton12.setBackground(s);
                }
                if(c == 2){
                    jButton11.setBackground(s);
                }
                if(c == 3){
                    jButton13.setBackground(s);
                }
                if(c == 4){
                    jButton8.setBackground(s);
                }
                if(c == 5){
                    jButton9.setBackground(s);
                }
                if(c == 6){
                    jButton10.setBackground(s);
                }
                if(c == 7){
                    jButton76.setBackground(s);
                }
                if(c == 8){
                    jButton77.setBackground(s);
                }
            }
            if(l == 3 && mainline == 3){
                if(c == 1){
                    jButton19.setBackground(s);
                }
                if(c == 2){
                    jButton18.setBackground(s);
                }
                if(c == 3){
                    jButton17.setBackground(s);
                }
                if(c == 4){
                    jButton16.setBackground(s);
                }
                if(c == 5){
                    jButton15.setBackground(s);
                }
                if(c == 6){
                    jButton14.setBackground(s);
                }
                if(c == 7){
                    jButton78.setBackground(s);
                }
                if(c == 8){
                    jButton79.setBackground(s);
                }
            }
            if(l == 4 && mainline == 4){
                if(c == 1){
                    jButton25.setBackground(s);
                }
                if(c == 2){
                    jButton24.setBackground(s);
                }
                if(c == 3){
                    jButton23.setBackground(s);
                }
                if(c == 4){
                    jButton22.setBackground(s);
                }
                if(c == 5){
                    jButton21.setBackground(s);
                }
                if(c == 6){
                    jButton20.setBackground(s);
                }
                if(c == 7){
                    jButton80.setBackground(s);
                }
                if(c == 8){
                    jButton81.setBackground(s);
                }
            }
            if(l == 5 && mainline == 5){
                if(c == 1){
                    jButton31.setBackground(s);
                }
                if(c == 2){
                    jButton30.setBackground(s);
                }
                if(c == 3){
                    jButton29.setBackground(s);
                }
                if(c == 4){
                    jButton28.setBackground(s);
                }
                if(c == 5){
                    jButton27.setBackground(s);
                }
                if(c == 6){
                    jButton26.setBackground(s);
                }
                if(c == 7){
                    jButton82.setBackground(s);
                }
                if(c == 8){
                    jButton83.setBackground(s);
                }
            }
            if(l == 6 && mainline == 6){
                if(c == 1){
                    jButton37.setBackground(s);
                }
                if(c == 2){
                    jButton36.setBackground(s);
                }
                if(c == 3){
                    jButton35.setBackground(s);
                }
                if(c == 4){
                    jButton34.setBackground(s);
                }
                if(c == 5){
                    jButton33.setBackground(s);
                }
                if(c == 6){
                    jButton32.setBackground(s);
                }
                if(c == 7){
                    jButton84.setBackground(s);
                }
                if(c == 8){
                    jButton85.setBackground(s);
                }
            } 
        }
        
        
        
    }
    
//Bulmaca ekranini default hale getirir.
    public void paintDefault(){
        Color color = new Color(153,153,153);
        int i, j;
        for(i=0; i<satirSayisi; i++){
            for(j=0; j<6; j++){
                paint(i+1, j+1, panel, color);
                write(i+1, j+1, panel, "");
            }
        }
    }
    
    //Denklem ureten fonskiyon.
    //Operator sayisina gore uretilen random sayilar olasılıkları duzenlemek icin verilmistir. Random gelen sayiyla birlikte denklem uzunlugu saglanmazsa
    //o denklem uretilmeyecektir. En yuksek ihtimal sirasiyla 1, 2 ve 3 operator gelmesidir.
    public String generateEquation(){
        int number1=0, number2=0, number3=0, number4=0, result=0;
        String numbers, equation="";
        int operatorChoise, operatorNumber;
        Random random = new Random();
        String[] operators = new String[4];
        String operator1, operator2="", operator3="";

        String equal = "=";
        operators[0] = "+"; operators[1] = "-"; operators[2] = "*"; operators[3] = "/";
        
        equation="";
        
        operatorChoise = random.nextInt(4);
        operator1 = operators[operatorChoise];
        operatorNumber = random.nextInt(3) + 1;
        
        // 1 adet işlem varsa
        if(operatorNumber == 1){
            number1 = random.nextInt(999) + 1;
            if(operator1.equals("+")){ 
                number2 = random.nextInt(999) + 1;
            }
            else if(operator1.equals("-")){
                number2 = random.nextInt(999) + 1;
            }
            //Ihtimalleri esitlemek icin bu ayarlamalar yapıldı. Testler sonucu +, -, *, / yakasik esit miktarda cikiyor.
            // 1 adet işlem varsa ve işlem çarpmaysa, sayı 2 haneli ise diger sayi maksimum iki haneli olabilir.
            // Sonucu kesinlestirmemek ve olasılıkları degistirmemek icin 500 degeri verilmistir.
            else if(operator1.equals("*")){
                if(Integer.toString(number1).length() == 1){
                    number2 = random.nextInt(999) + 1;
                }
                if(Integer.toString(number1).length() == 2){
                    number2 = random.nextInt(500) + 1;
                }
                if(Integer.toString(number1).length() == 3){
                    number2 = random.nextInt(150) + 1;
                }
                
            }
            else if(operator1.equals("/")){
                number2 = random.nextInt(number1) + 1;
            }
            numbers = Integer.toString(number1);
            equation += numbers;
            equation += operator1;
            numbers = Integer.toString(number2);
            equation += numbers;
        }
        if(operatorNumber == 2){ 
            equation = "";
            number1 = random.nextInt(999) + 1;
            if(Integer.toString(number1).length() == 3){ 
                number2 = random.nextInt(50) + 1;
                number3 = random.nextInt(50) + 1;
            }
            else if(Integer.toString(number1).length() == 2){
                number2 = random.nextInt(120) + 1;
                number3 = random.nextInt(120) + 1;
            }
            else if(Integer.toString(number1).length() == 1){
                number2 = random.nextInt(99) + 1;
                if(Integer.toString(number2).length() == 3){
                        number3 = random.nextInt(9) + 1;
                    }
                else{
                    number3 = random.nextInt(99) + 1;
                }
            }
            numbers = Integer.toString(number1);
            equation += numbers;
            equation += operator1;
            numbers = Integer.toString(number2);
            equation += numbers;
            
            operatorChoise = random.nextInt(4);
            operator2 = operators[operatorChoise];
            number3 = random.nextInt(9) + 1;
            equation += operator2;
            numbers = Integer.toString(number3);
            equation += numbers;
            
            
        }
        if(operatorNumber == 3){ 
            equation = "";
            number1 = random.nextInt(50) + 1;
            number2 = random.nextInt(50) + 1;
            numbers = Integer.toString(number1);
            equation += numbers;
            equation += operator1;
            numbers = Integer.toString(number2);
            equation += numbers;
            
            operatorChoise = random.nextInt(4);
            operator2 = operators[operatorChoise];
            number3 = random.nextInt(50) + 1;
            equation += operator2;
            numbers = Integer.toString(number3);
            equation += numbers;
            
            operatorChoise = random.nextInt(4);
            operator3 = operators[operatorChoise];
            number4 = random.nextInt(50) + 1;
            equation += operator2;
            numbers = Integer.toString(number4);
            equation += numbers;
        }

        if(operator1 == "+"){
            if(operatorNumber != 1){
                if(operator2 == "+"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 + number2 + number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 + number2 + number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 + number2 + number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                                equation = null;
                            }
                            else {
                            	result = number1 + number2 + number3 / number4;
                            }
                            
                        }
                    }
                    else{
                        result = number1 + number2 + number3;
                    } 
                }
                else if(operator2 == "-"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 + number2 - number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 + number2 - number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 + number2 - number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 + number2 - number3 / number4;
                            }
                            
                        }
                    }
                    else{
                        result = number1 + number2 - number3;
                    }
                }
                else if(operator2 == "*"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 + number2 * number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 + number2 * number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 + number2 * number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 + number2 * number3 / number4;
                            }
                            
                        }
                    }
                    else{
                        result = number1 + number2 * number3;
                    }
                    
                }
                else{
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 + number2 / number3 + number4;
                            }   
                        }
                        if(operator3 == "-"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 + number2 / number3 - number4;
                            }
                        }
                        if(operator3 == "*"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            else {
                            	 result = number1 + number2 / number3 * number4;
                            }
                        }
                        if(operator3 == "/"){
                            if((number2 % number3) != 0 || (number2 / number3) % number4 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 + number2 / number3 / number4;
                            }
                            
                        }
                    }
                    else{
                        if(number2 % number3 != 0){
                        	equation = null;
                        }
                        else {
                        	result = number1 + number2 / number3;
                        }
                    }
                    
                }
            }
            else{
                result = number1 + number2;
            }

            if(equation == null) {
            	return generateEquation();
            }
            else {
            	equation += equal;
	            numbers = Integer.toString(result);
	            equation += numbers;
	            if(equation.length() < 7 || equation.length() > 9){
	                return generateEquation();
	            }
	            else{
	                return equation;
                    }
            }

        }
        else if(operator1 == "-"){
            if(operatorNumber != 1){
                if(operator2 == "+"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 - number2 + number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 - number2 + number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 - number2 + number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 - number2 + number3 / number4;
                            }   
                        }
                    }
                    else{
                        result = number1 - number2 + number3;
                    } 
                }
                else if(operator2 == "-"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 - number2 - number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 - number2 - number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 - number2 - number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 - number2 - number3 / number4;
                            }
                        }
                    }
                    else{
                        result = number1 - number2 - number3;
                    }
                }
                else if(operator2 == "*"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 - number2 * number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 - number2 * number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 - number2 * number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 - number2 * number3 / number4;
                            }
                        }
                    }
                    else{
                        result = number1 - number2 * number3;
                    }
                    
                }
                else{
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 - number2 / number3 + number4;
                            }
                            
                        }
                        if(operator3 == "-"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 - number2 / number3 - number4;
                            }
                            
                        }
                        if(operator3 == "*"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 - number2 / number3 * number4;
                            }
                            
                        }
                        if(operator3 == "/"){
                            if((number1 % number2) != 0 || (number2 / number3) % number4 != 0){
                            	equation = null;
                            }
                            else {
                            	result = number1 - number2 / number3 / number4;
                            }
                            
                        }
                    }
                    else{
                        if(number2 % number3 != 0){
                        	equation = null;
                        }
                        else {
                        	result = number1 - number2 / number3;
                        }
                    }
                    
                }
            }
            else{
                result = number1 - number2;
            }

            if(equation == null) {
            	return generateEquation();
            }
            else {
            	equation += equal;
	            numbers = Integer.toString(result);
	            equation += numbers;
	            if(equation.length() < 7 || equation.length() > 9){
	                return generateEquation();
	            }
	            else{
	                return equation;
                    }
            }
        }
        else if(operator1 == "*"){

            if(operatorNumber != 1){
                if(operator2 == "+"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 * number2 + number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 * number2 + number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 * number2 + number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            result = number1 * number2 + number3 / number4;
                        }
                    }
                    else{
                        result = number1 * number2 + number3;
                    } 
                }
                else if(operator2 == "-"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 * number2 - number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 * number2 - number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 * number2 - number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            result = number1 * number2 - number3 / number4;
                        }
                    }
                    else{
                        result = number1 * number2 - number3;
                    }
                }
                else if(operator2 == "*"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            result = number1 * number2 * number3 + number4;
                        }
                        if(operator3 == "-"){
                            result = number1 * number2 * number3 - number4;
                        }
                        if(operator3 == "*"){
                            result = number1 * number2 * number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number3 % number4 != 0){
                            	equation = null;
                            }
                            result = number1 * number2 * number3 / number4;
                        }
                    }
                    else{
                        result = number1 * number2 * number3;
                    }
                    
                }
                else{
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            result = number1 * number2 / number3 + number4;
                        }
                        if(operator3 == "-"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            result = number1 * number2 / number3 - number4;
                        }
                        if(operator3 == "*"){
                            if(number2 % number3 != 0){
                            	equation = null;
                            }
                            result = number1 * number2 / number3 * number4;
                        }
                        if(operator3 == "/"){
                            if((number2 % number3) != 0 || (number2 / number3) % number4 != 0){
                            	equation = null;
                            }
                            result = number1 * number2 / number3 / number4;
                        }
                    }
                    else{
                        if(number2 % number3 != 0){
                        	equation = null;
                        }
                        result = number1 * number2 / number3;
                    }
                    
                }
            }
            else{
                result = number1 * number2;
            }

            if(equation == null) {
            	return generateEquation();
            }
            else {
            	equation += equal;
	            numbers = Integer.toString(result);
	            equation += numbers;
	            if(equation.length() < 7 || equation.length() > 9){
	                return generateEquation();
	            }
	            else{
	                return equation;
                    }
            }

        }
        else if(operator1 == "/"){

            if(operatorNumber != 1){
                if(operator2 == "+"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 + number3 + number4;
                        }
                        if(operator3 == "-"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 + number3 - number4;
                        }
                        if(operator3 == "*"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 + number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number1 % number2 != 0 || number3 % number4 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 + number3 / number4;
                        }
                    }
                    else{
                        if(number1 % number2 != 0){
                        	equation = null;
                            }
                        result = number1 / number2 + number3;
                    } 
                }
                else if(operator2 == "-"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 - number3 + number4;
                        }
                        if(operator3 == "-"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 - number3 - number4;
                        }
                        if(operator3 == "*"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 - number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number1 % number2 != 0 || number3 % number4 !=0){
                            	equation = null;
                            }
                            result = number1 / number2 - number3 / number4;
                        }
                    }
                    else{
                        if(number1 % number2 != 0){
                        	equation = null;
                            }
                        result = number1 / number2 - number3;
                    }
                }
                else if(operator2 == "*"){
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 * number3 + number4;
                        }
                        if(operator3 == "-"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 * number3 - number4;
                        }
                        if(operator3 == "*"){
                            if(number1 % number2 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 * number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number1 % number2 != 0 || (number1 / number2 * number3) % number4 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 * number3 / number4;
                        }
                    }
                    else{
                        if(number1 % number2 != 0){
                        	equation = null;
                        }
                        result = number1 / number2 * number3;
                    }
                    
                }
                else{
                    if(operatorNumber == 3){
                        if(operator3 == "+"){
                            if((number1 % number2) != 0 || (number1 / number2) % number3 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 / number3 + number4;
                        }
                        if(operator3 == "-"){
                            if((number1 % number2) != 0 || (number1 / number2) % number3 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 / number3 - number4;
                        }
                        if(operator3 == "*"){
                            if((number1 % number2) != 0 || (number1 / number2) % number3 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 / number3 * number4;
                        }
                        if(operator3 == "/"){
                            if(number1 % number2 != 0 || (number1 / number2) % number3 != 0 
                                     || ((number1 / number2) / number3) % number4 != 0){
                            	equation = null;
                            }
                            result = number1 / number2 / number3 / number4;
                        }
                    }
                    else{
                        if(number1 % number2 != 0 || (number1 / number2) % number3 != 0){
                        	equation = null;
                        }
                        result = number1 / number2 / number3;
                    }
                    
                }
            }
            else{
                if(number1 % number2 != 0){
                    equation = null;
                }
                result = number1 / number2;
            }

            if(equation == null) {
            	return generateEquation();
            }
            else {
            	equation += equal;
	            numbers = Integer.toString(result);
	            equation += numbers;
	            if(equation.length() < 7 || equation.length() > 9){
	                return generateEquation();
	            }
	            else{
	                return equation;
	            }
            }

        }
        return null;
    }
    
    //Oyunun saatini baslatmak icin gerekn fonksiyon.
    public void startTimer(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int j = 1;
                int i=0;
                Boolean s = true;
                String ss;
                try{
                    while(s){
                        if(i == 60){
                            minutes.setText(Integer.toString(j));
                            seconds.setText(Integer.toString(0));
                            j++;
                            i = 1;
                        }
                        else{
                            seconds.setText(Integer.toString(i));
                            i++;
                        }
                        Thread.sleep(1000);
                        if(timeControl == 1){
                            j=1;
                            i=0;
                            seconds.setText(Integer.toString(0));
                            minutes.setText(Integer.toString(0));
                        }
                        if(timeControl == -1){
                            j = Integer.parseInt(minutes.getText()) + 1;
                            i = Integer.parseInt(seconds.getText());
                            timeControl = 0;
                        }
                    }
                    
                }
                catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        
        
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDialog1 = new javax.swing.JDialog();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        footer3 = new javax.swing.JLabel();
        jDialog2 = new javax.swing.JDialog();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton135 = new javax.swing.JButton();
        footer4 = new javax.swing.JLabel();
        jDialog3 = new javax.swing.JDialog();
        jLabel6 = new javax.swing.JLabel();
        jButton134 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        footer5 = new javax.swing.JLabel();
        jDialog4 = new javax.swing.JDialog();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        footer6 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        jButton32 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jButton74 = new javax.swing.JButton();
        jButton75 = new javax.swing.JButton();
        jButton76 = new javax.swing.JButton();
        jButton77 = new javax.swing.JButton();
        jButton78 = new javax.swing.JButton();
        jButton79 = new javax.swing.JButton();
        jButton80 = new javax.swing.JButton();
        jButton81 = new javax.swing.JButton();
        jButton82 = new javax.swing.JButton();
        jButton83 = new javax.swing.JButton();
        jButton84 = new javax.swing.JButton();
        jButton85 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton38 = new javax.swing.JButton();
        jButton39 = new javax.swing.JButton();
        jButton40 = new javax.swing.JButton();
        jButton41 = new javax.swing.JButton();
        jButton42 = new javax.swing.JButton();
        jButton43 = new javax.swing.JButton();
        jButton44 = new javax.swing.JButton();
        jButton45 = new javax.swing.JButton();
        jButton46 = new javax.swing.JButton();
        jButton47 = new javax.swing.JButton();
        jButton48 = new javax.swing.JButton();
        jButton49 = new javax.swing.JButton();
        jButton50 = new javax.swing.JButton();
        jButton51 = new javax.swing.JButton();
        jButton52 = new javax.swing.JButton();
        jButton53 = new javax.swing.JButton();
        jButton54 = new javax.swing.JButton();
        jButton55 = new javax.swing.JButton();
        jButton56 = new javax.swing.JButton();
        jButton57 = new javax.swing.JButton();
        jButton58 = new javax.swing.JButton();
        jButton59 = new javax.swing.JButton();
        jButton60 = new javax.swing.JButton();
        jButton61 = new javax.swing.JButton();
        jButton62 = new javax.swing.JButton();
        jButton63 = new javax.swing.JButton();
        jButton64 = new javax.swing.JButton();
        jButton65 = new javax.swing.JButton();
        jButton66 = new javax.swing.JButton();
        jButton67 = new javax.swing.JButton();
        jButton68 = new javax.swing.JButton();
        jButton69 = new javax.swing.JButton();
        jButton70 = new javax.swing.JButton();
        jButton71 = new javax.swing.JButton();
        jButton72 = new javax.swing.JButton();
        jButton73 = new javax.swing.JButton();
        jButton137 = new javax.swing.JButton();
        jButton138 = new javax.swing.JButton();
        jButton139 = new javax.swing.JButton();
        jButton140 = new javax.swing.JButton();
        jButton141 = new javax.swing.JButton();
        jButton142 = new javax.swing.JButton();
        jButton143 = new javax.swing.JButton();
        jButton144 = new javax.swing.JButton();
        jButton145 = new javax.swing.JButton();
        jButton146 = new javax.swing.JButton();
        jButton147 = new javax.swing.JButton();
        jButton148 = new javax.swing.JButton();
        jButton149 = new javax.swing.JButton();
        jButton150 = new javax.swing.JButton();
        jButton151 = new javax.swing.JButton();
        jButton152 = new javax.swing.JButton();
        jButton153 = new javax.swing.JButton();
        jButton154 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jButton86 = new javax.swing.JButton();
        jButton87 = new javax.swing.JButton();
        jButton88 = new javax.swing.JButton();
        jButton89 = new javax.swing.JButton();
        jButton90 = new javax.swing.JButton();
        jButton91 = new javax.swing.JButton();
        jButton92 = new javax.swing.JButton();
        jButton93 = new javax.swing.JButton();
        jButton94 = new javax.swing.JButton();
        jButton95 = new javax.swing.JButton();
        jButton96 = new javax.swing.JButton();
        jButton97 = new javax.swing.JButton();
        jButton98 = new javax.swing.JButton();
        jButton99 = new javax.swing.JButton();
        jButton100 = new javax.swing.JButton();
        jButton101 = new javax.swing.JButton();
        jButton102 = new javax.swing.JButton();
        jButton103 = new javax.swing.JButton();
        jButton104 = new javax.swing.JButton();
        jButton105 = new javax.swing.JButton();
        jButton106 = new javax.swing.JButton();
        jButton107 = new javax.swing.JButton();
        jButton108 = new javax.swing.JButton();
        jButton109 = new javax.swing.JButton();
        jButton110 = new javax.swing.JButton();
        jButton111 = new javax.swing.JButton();
        jButton112 = new javax.swing.JButton();
        jButton113 = new javax.swing.JButton();
        jButton114 = new javax.swing.JButton();
        jButton115 = new javax.swing.JButton();
        jButton116 = new javax.swing.JButton();
        jButton117 = new javax.swing.JButton();
        jButton118 = new javax.swing.JButton();
        jButton119 = new javax.swing.JButton();
        jButton120 = new javax.swing.JButton();
        jButton121 = new javax.swing.JButton();
        jButton122 = new javax.swing.JButton();
        jButton124 = new javax.swing.JButton();
        jButton126 = new javax.swing.JButton();
        jButton128 = new javax.swing.JButton();
        jButton130 = new javax.swing.JButton();
        jButton132 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton123 = new javax.swing.JButton();
        jButton125 = new javax.swing.JButton();
        footer2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        sec13 = new javax.swing.JButton();
        sec10 = new javax.swing.JButton();
        sec14 = new javax.swing.JButton();
        sec11 = new javax.swing.JButton();
        sec15 = new javax.swing.JButton();
        sec12 = new javax.swing.JButton();
        sec16 = new javax.swing.JButton();
        jButton127 = new javax.swing.JButton();
        sec17 = new javax.swing.JButton();
        jButton129 = new javax.swing.JButton();
        sec4 = new javax.swing.JButton();
        jButton131 = new javax.swing.JButton();
        sec5 = new javax.swing.JButton();
        sec6 = new javax.swing.JButton();
        sec7 = new javax.swing.JButton();
        sec8 = new javax.swing.JButton();
        sec9 = new javax.swing.JButton();
        sec1 = new javax.swing.JButton();
        seconds = new javax.swing.JLabel();
        minutes = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jButton136 = new javax.swing.JButton();
        anaSayfa2 = new javax.swing.JButton();
        footer1 = new javax.swing.JLabel();
        anaSayfa1 = new javax.swing.JButton();
        twoDot = new javax.swing.JLabel();
        footer = new javax.swing.JLabel();

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel4.setText("Lutfen tum boslukları");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel5.setText("doldurunuz.");

        footer3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        footer3.setForeground(new java.awt.Color(51, 153, 255));
        footer3.setText("19011034-Eren Duman");

        javax.swing.GroupLayout jDialog1Layout = new javax.swing.GroupLayout(jDialog1.getContentPane());
        jDialog1.getContentPane().setLayout(jDialog1Layout);
        jDialog1Layout.setHorizontalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog1Layout.createSequentialGroup()
                        .addGroup(jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel4))
                            .addGroup(jDialog1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addComponent(jLabel5)))
                        .addGap(0, 134, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(footer3)))
                .addContainerGap())
        );
        jDialog1Layout.setVerticalGroup(
            jDialog1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 65, Short.MAX_VALUE)
                .addComponent(footer3)
                .addContainerGap())
        );

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel3.setText("Kaybettiniz.");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel7.setText("jLabel6");

        jButton135.setText("Ana Sayfa");
        jButton135.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton135ActionPerformed(evt);
            }
        });

        footer4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        footer4.setForeground(new java.awt.Color(51, 153, 255));
        footer4.setText("19011034-Eren Duman");

        javax.swing.GroupLayout jDialog2Layout = new javax.swing.GroupLayout(jDialog2.getContentPane());
        jDialog2.getContentPane().setLayout(jDialog2Layout);
        jDialog2Layout.setHorizontalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog2Layout.createSequentialGroup()
                .addGroup(jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel3))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel7))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jButton135))
                    .addGroup(jDialog2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(footer4)))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        jDialog2Layout.setVerticalGroup(
            jDialog2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jDialog2Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(jLabel7)
                .addGap(18, 18, 18)
                .addComponent(jButton135)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(footer4)
                .addGap(117, 117, 117))
        );

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel6.setText("Tebrikler, kazandıniz.");

        jButton134.setText("Ana Sayfa");
        jButton134.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton134ActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel14.setText("jLabel14");

        footer5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        footer5.setForeground(new java.awt.Color(51, 153, 255));
        footer5.setText("19011034-Eren Duman");

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addGroup(jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jButton134))
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel6))
                    .addGroup(jDialog3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(footer5))
                .addContainerGap(46, Short.MAX_VALUE))
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog3Layout.createSequentialGroup()
                .addGap(57, 57, 57)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton134, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(footer5)
                .addContainerGap(116, Short.MAX_VALUE))
        );

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel8.setText("Girdiginiz denklemin iki");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel9.setText("tarafi uyusmamaktadir.");

        footer6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        footer6.setForeground(new java.awt.Color(51, 153, 255));
        footer6.setText("19011034-Eren Duman");

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9)
                    .addComponent(footer6))
                .addContainerGap(211, Short.MAX_VALUE))
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jDialog4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(footer6)
                .addContainerGap(173, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(775, 725));

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton7.setBackground(new java.awt.Color(153, 153, 153));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(153, 153, 153));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(153, 153, 153));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setBackground(new java.awt.Color(153, 153, 153));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setBackground(new java.awt.Color(153, 153, 153));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setBackground(new java.awt.Color(153, 153, 153));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(153, 153, 153));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(153, 153, 153));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setBackground(new java.awt.Color(153, 153, 153));
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setBackground(new java.awt.Color(153, 153, 153));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton12.setBackground(new java.awt.Color(153, 153, 153));
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setBackground(new java.awt.Color(153, 153, 153));
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jButton19.setBackground(new java.awt.Color(153, 153, 153));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(153, 153, 153));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(153, 153, 153));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton15.setBackground(new java.awt.Color(153, 153, 153));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setBackground(new java.awt.Color(153, 153, 153));
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton14.setBackground(new java.awt.Color(153, 153, 153));
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton20.setBackground(new java.awt.Color(153, 153, 153));
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setBackground(new java.awt.Color(153, 153, 153));
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(153, 153, 153));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        jButton23.setBackground(new java.awt.Color(153, 153, 153));
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setBackground(new java.awt.Color(153, 153, 153));
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jButton25.setBackground(new java.awt.Color(153, 153, 153));
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setBackground(new java.awt.Color(153, 153, 153));
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jButton27.setBackground(new java.awt.Color(153, 153, 153));
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setBackground(new java.awt.Color(153, 153, 153));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setBackground(new java.awt.Color(153, 153, 153));
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton30.setBackground(new java.awt.Color(153, 153, 153));
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton31.setBackground(new java.awt.Color(153, 153, 153));
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jButton32.setBackground(new java.awt.Color(153, 153, 153));
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jButton33.setBackground(new java.awt.Color(153, 153, 153));
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton34.setBackground(new java.awt.Color(153, 153, 153));
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jButton35.setBackground(new java.awt.Color(153, 153, 153));
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        jButton36.setBackground(new java.awt.Color(153, 153, 153));
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        jButton37.setBackground(new java.awt.Color(153, 153, 153));
        jButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton37ActionPerformed(evt);
            }
        });

        jButton74.setBackground(new java.awt.Color(153, 153, 153));
        jButton74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton74ActionPerformed(evt);
            }
        });

        jButton75.setBackground(new java.awt.Color(153, 153, 153));
        jButton75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton75ActionPerformed(evt);
            }
        });

        jButton76.setBackground(new java.awt.Color(153, 153, 153));
        jButton76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton76ActionPerformed(evt);
            }
        });

        jButton77.setBackground(new java.awt.Color(153, 153, 153));
        jButton77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton77ActionPerformed(evt);
            }
        });

        jButton78.setBackground(new java.awt.Color(153, 153, 153));
        jButton78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton78ActionPerformed(evt);
            }
        });

        jButton79.setBackground(new java.awt.Color(153, 153, 153));
        jButton79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton79ActionPerformed(evt);
            }
        });

        jButton80.setBackground(new java.awt.Color(153, 153, 153));
        jButton80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton80ActionPerformed(evt);
            }
        });

        jButton81.setBackground(new java.awt.Color(153, 153, 153));
        jButton81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton81ActionPerformed(evt);
            }
        });

        jButton82.setBackground(new java.awt.Color(153, 153, 153));
        jButton82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton82ActionPerformed(evt);
            }
        });

        jButton83.setBackground(new java.awt.Color(153, 153, 153));
        jButton83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton83ActionPerformed(evt);
            }
        });

        jButton84.setBackground(new java.awt.Color(153, 153, 153));
        jButton84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton84ActionPerformed(evt);
            }
        });

        jButton85.setBackground(new java.awt.Color(153, 153, 153));
        jButton85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton85ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton84, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton85, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton76, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton78, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton79, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton80, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton81, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton83, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton74, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton75, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton76, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton77, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton19, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton78, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton79, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton23, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton22, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton21, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton80, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton81, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton26, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton82, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton83, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton37, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton36, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton35, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton34, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton33, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton32, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton84, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton85, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton38.setBackground(new java.awt.Color(153, 153, 153));
        jButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton38ActionPerformed(evt);
            }
        });

        jButton39.setBackground(new java.awt.Color(153, 153, 153));
        jButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton39ActionPerformed(evt);
            }
        });

        jButton40.setBackground(new java.awt.Color(153, 153, 153));
        jButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton40ActionPerformed(evt);
            }
        });

        jButton41.setBackground(new java.awt.Color(153, 153, 153));
        jButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton41ActionPerformed(evt);
            }
        });

        jButton42.setBackground(new java.awt.Color(153, 153, 153));
        jButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton42ActionPerformed(evt);
            }
        });

        jButton43.setBackground(new java.awt.Color(153, 153, 153));
        jButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton43ActionPerformed(evt);
            }
        });

        jButton44.setBackground(new java.awt.Color(153, 153, 153));
        jButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton44ActionPerformed(evt);
            }
        });

        jButton45.setBackground(new java.awt.Color(153, 153, 153));
        jButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton45ActionPerformed(evt);
            }
        });

        jButton46.setBackground(new java.awt.Color(153, 153, 153));
        jButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton46ActionPerformed(evt);
            }
        });

        jButton47.setBackground(new java.awt.Color(153, 153, 153));
        jButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton47ActionPerformed(evt);
            }
        });

        jButton48.setBackground(new java.awt.Color(153, 153, 153));
        jButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton48ActionPerformed(evt);
            }
        });

        jButton49.setBackground(new java.awt.Color(153, 153, 153));
        jButton49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton49ActionPerformed(evt);
            }
        });

        jButton50.setBackground(new java.awt.Color(153, 153, 153));
        jButton50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton50ActionPerformed(evt);
            }
        });

        jButton51.setBackground(new java.awt.Color(153, 153, 153));
        jButton51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton51ActionPerformed(evt);
            }
        });

        jButton52.setBackground(new java.awt.Color(153, 153, 153));
        jButton52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton52ActionPerformed(evt);
            }
        });

        jButton53.setBackground(new java.awt.Color(153, 153, 153));
        jButton53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton53ActionPerformed(evt);
            }
        });

        jButton54.setBackground(new java.awt.Color(153, 153, 153));
        jButton54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton54ActionPerformed(evt);
            }
        });

        jButton55.setBackground(new java.awt.Color(153, 153, 153));
        jButton55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton55ActionPerformed(evt);
            }
        });

        jButton56.setBackground(new java.awt.Color(153, 153, 153));
        jButton56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton56ActionPerformed(evt);
            }
        });

        jButton57.setBackground(new java.awt.Color(153, 153, 153));
        jButton57.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton57ActionPerformed(evt);
            }
        });

        jButton58.setBackground(new java.awt.Color(153, 153, 153));
        jButton58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton58ActionPerformed(evt);
            }
        });

        jButton59.setBackground(new java.awt.Color(153, 153, 153));
        jButton59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton59ActionPerformed(evt);
            }
        });

        jButton60.setBackground(new java.awt.Color(153, 153, 153));
        jButton60.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton60ActionPerformed(evt);
            }
        });

        jButton61.setBackground(new java.awt.Color(153, 153, 153));
        jButton61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton61ActionPerformed(evt);
            }
        });

        jButton62.setBackground(new java.awt.Color(153, 153, 153));
        jButton62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton62ActionPerformed(evt);
            }
        });

        jButton63.setBackground(new java.awt.Color(153, 153, 153));
        jButton63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton63ActionPerformed(evt);
            }
        });

        jButton64.setBackground(new java.awt.Color(153, 153, 153));
        jButton64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton64ActionPerformed(evt);
            }
        });

        jButton65.setBackground(new java.awt.Color(153, 153, 153));
        jButton65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton65ActionPerformed(evt);
            }
        });

        jButton66.setBackground(new java.awt.Color(153, 153, 153));
        jButton66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton66ActionPerformed(evt);
            }
        });

        jButton67.setBackground(new java.awt.Color(153, 153, 153));
        jButton67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton67ActionPerformed(evt);
            }
        });

        jButton68.setBackground(new java.awt.Color(153, 153, 153));
        jButton68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton68ActionPerformed(evt);
            }
        });

        jButton69.setBackground(new java.awt.Color(153, 153, 153));
        jButton69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton69ActionPerformed(evt);
            }
        });

        jButton70.setBackground(new java.awt.Color(153, 153, 153));
        jButton70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton70ActionPerformed(evt);
            }
        });

        jButton71.setBackground(new java.awt.Color(153, 153, 153));
        jButton71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton71ActionPerformed(evt);
            }
        });

        jButton72.setBackground(new java.awt.Color(153, 153, 153));
        jButton72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton72ActionPerformed(evt);
            }
        });

        jButton73.setBackground(new java.awt.Color(153, 153, 153));
        jButton73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton73ActionPerformed(evt);
            }
        });

        jButton137.setBackground(new java.awt.Color(153, 153, 153));
        jButton137.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton137ActionPerformed(evt);
            }
        });

        jButton138.setBackground(new java.awt.Color(153, 153, 153));
        jButton138.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton138ActionPerformed(evt);
            }
        });

        jButton139.setBackground(new java.awt.Color(153, 153, 153));
        jButton139.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton139ActionPerformed(evt);
            }
        });

        jButton140.setBackground(new java.awt.Color(153, 153, 153));
        jButton140.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton140ActionPerformed(evt);
            }
        });

        jButton141.setBackground(new java.awt.Color(153, 153, 153));
        jButton141.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton141ActionPerformed(evt);
            }
        });

        jButton142.setBackground(new java.awt.Color(153, 153, 153));
        jButton142.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton142ActionPerformed(evt);
            }
        });

        jButton143.setBackground(new java.awt.Color(153, 153, 153));
        jButton143.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton143ActionPerformed(evt);
            }
        });

        jButton144.setBackground(new java.awt.Color(153, 153, 153));
        jButton144.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton144ActionPerformed(evt);
            }
        });

        jButton145.setBackground(new java.awt.Color(153, 153, 153));
        jButton145.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton145ActionPerformed(evt);
            }
        });

        jButton146.setBackground(new java.awt.Color(153, 153, 153));
        jButton146.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton146ActionPerformed(evt);
            }
        });

        jButton147.setBackground(new java.awt.Color(153, 153, 153));
        jButton147.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton147ActionPerformed(evt);
            }
        });

        jButton148.setBackground(new java.awt.Color(153, 153, 153));
        jButton148.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton148ActionPerformed(evt);
            }
        });

        jButton149.setBackground(new java.awt.Color(153, 153, 153));
        jButton149.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton149ActionPerformed(evt);
            }
        });

        jButton150.setBackground(new java.awt.Color(153, 153, 153));
        jButton150.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton150ActionPerformed(evt);
            }
        });

        jButton151.setBackground(new java.awt.Color(153, 153, 153));
        jButton151.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton151ActionPerformed(evt);
            }
        });

        jButton152.setBackground(new java.awt.Color(153, 153, 153));
        jButton152.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton152ActionPerformed(evt);
            }
        });

        jButton153.setBackground(new java.awt.Color(153, 153, 153));
        jButton153.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton153ActionPerformed(evt);
            }
        });

        jButton154.setBackground(new java.awt.Color(153, 153, 153));
        jButton154.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton154ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton49, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton52, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton55, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton139, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton138, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButton146, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton150, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(jButton149, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton137, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton141, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton142, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton140, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton147, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton151, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton152, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton67, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton66, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton144, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton145, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton143, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton148, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton153, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton154, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton42, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton41, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton43, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton38, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton39, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton40, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton139, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton149, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton137, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton48, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton47, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton49, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton44, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton45, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton46, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton50, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton51, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton52, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton54, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton53, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton55, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton61, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton60, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton59, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton58, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton57, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton56, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton138, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton146, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton150, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton140, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton141, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton142, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton147, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton152, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jButton151, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton67, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton66, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton65, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton64, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton63, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton62, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton73, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton72, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton71, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton70, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton69, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton68, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton143, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton144, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton145, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton148, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton154, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton153, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton86.setBackground(new java.awt.Color(153, 153, 153));
        jButton86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton86ActionPerformed(evt);
            }
        });

        jButton87.setBackground(new java.awt.Color(153, 153, 153));
        jButton87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton87ActionPerformed(evt);
            }
        });

        jButton88.setBackground(new java.awt.Color(153, 153, 153));
        jButton88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton88ActionPerformed(evt);
            }
        });

        jButton89.setBackground(new java.awt.Color(153, 153, 153));
        jButton89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton89ActionPerformed(evt);
            }
        });

        jButton90.setBackground(new java.awt.Color(153, 153, 153));
        jButton90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton90ActionPerformed(evt);
            }
        });

        jButton91.setBackground(new java.awt.Color(153, 153, 153));
        jButton91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton91ActionPerformed(evt);
            }
        });

        jButton92.setBackground(new java.awt.Color(153, 153, 153));
        jButton92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton92ActionPerformed(evt);
            }
        });

        jButton93.setBackground(new java.awt.Color(153, 153, 153));
        jButton93.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton93ActionPerformed(evt);
            }
        });

        jButton94.setBackground(new java.awt.Color(153, 153, 153));
        jButton94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton94ActionPerformed(evt);
            }
        });

        jButton95.setBackground(new java.awt.Color(153, 153, 153));
        jButton95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton95ActionPerformed(evt);
            }
        });

        jButton96.setBackground(new java.awt.Color(153, 153, 153));
        jButton96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton96ActionPerformed(evt);
            }
        });

        jButton97.setBackground(new java.awt.Color(153, 153, 153));
        jButton97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton97ActionPerformed(evt);
            }
        });

        jButton98.setBackground(new java.awt.Color(153, 153, 153));
        jButton98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton98ActionPerformed(evt);
            }
        });

        jButton99.setBackground(new java.awt.Color(153, 153, 153));
        jButton99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton99ActionPerformed(evt);
            }
        });

        jButton100.setBackground(new java.awt.Color(153, 153, 153));
        jButton100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton100ActionPerformed(evt);
            }
        });

        jButton101.setBackground(new java.awt.Color(153, 153, 153));
        jButton101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton101ActionPerformed(evt);
            }
        });

        jButton102.setBackground(new java.awt.Color(153, 153, 153));
        jButton102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton102ActionPerformed(evt);
            }
        });

        jButton103.setBackground(new java.awt.Color(153, 153, 153));
        jButton103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton103ActionPerformed(evt);
            }
        });

        jButton104.setBackground(new java.awt.Color(153, 153, 153));
        jButton104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton104ActionPerformed(evt);
            }
        });

        jButton105.setBackground(new java.awt.Color(153, 153, 153));
        jButton105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton105ActionPerformed(evt);
            }
        });

        jButton106.setBackground(new java.awt.Color(153, 153, 153));
        jButton106.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton106ActionPerformed(evt);
            }
        });

        jButton107.setBackground(new java.awt.Color(153, 153, 153));
        jButton107.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton107ActionPerformed(evt);
            }
        });

        jButton108.setBackground(new java.awt.Color(153, 153, 153));
        jButton108.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton108ActionPerformed(evt);
            }
        });

        jButton109.setBackground(new java.awt.Color(153, 153, 153));
        jButton109.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton109ActionPerformed(evt);
            }
        });

        jButton110.setBackground(new java.awt.Color(153, 153, 153));
        jButton110.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton110ActionPerformed(evt);
            }
        });

        jButton111.setBackground(new java.awt.Color(153, 153, 153));
        jButton111.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton111ActionPerformed(evt);
            }
        });

        jButton112.setBackground(new java.awt.Color(153, 153, 153));
        jButton112.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton112ActionPerformed(evt);
            }
        });

        jButton113.setBackground(new java.awt.Color(153, 153, 153));
        jButton113.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton113ActionPerformed(evt);
            }
        });

        jButton114.setBackground(new java.awt.Color(153, 153, 153));
        jButton114.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton114ActionPerformed(evt);
            }
        });

        jButton115.setBackground(new java.awt.Color(153, 153, 153));
        jButton115.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton115ActionPerformed(evt);
            }
        });

        jButton116.setBackground(new java.awt.Color(153, 153, 153));
        jButton116.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton116ActionPerformed(evt);
            }
        });

        jButton117.setBackground(new java.awt.Color(153, 153, 153));
        jButton117.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton117ActionPerformed(evt);
            }
        });

        jButton118.setBackground(new java.awt.Color(153, 153, 153));
        jButton118.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton118ActionPerformed(evt);
            }
        });

        jButton119.setBackground(new java.awt.Color(153, 153, 153));
        jButton119.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton119ActionPerformed(evt);
            }
        });

        jButton120.setBackground(new java.awt.Color(153, 153, 153));
        jButton120.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton120ActionPerformed(evt);
            }
        });

        jButton121.setBackground(new java.awt.Color(153, 153, 153));
        jButton121.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton121ActionPerformed(evt);
            }
        });

        jButton122.setBackground(new java.awt.Color(153, 153, 153));
        jButton122.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton122ActionPerformed(evt);
            }
        });

        jButton124.setBackground(new java.awt.Color(153, 153, 153));
        jButton124.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton124ActionPerformed(evt);
            }
        });

        jButton126.setBackground(new java.awt.Color(153, 153, 153));
        jButton126.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton126ActionPerformed(evt);
            }
        });

        jButton128.setBackground(new java.awt.Color(153, 153, 153));
        jButton128.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton128ActionPerformed(evt);
            }
        });

        jButton130.setBackground(new java.awt.Color(153, 153, 153));
        jButton130.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton130ActionPerformed(evt);
            }
        });

        jButton132.setBackground(new java.awt.Color(153, 153, 153));
        jButton132.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton132ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton90, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton89, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton91, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton86, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton87, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton88, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton122, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton96, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton95, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton97, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton92, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton93, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton94, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton124, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton98, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton99, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton100, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton102, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton101, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton103, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton126, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton109, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton108, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton107, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton106, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton105, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton104, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton128, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton115, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton114, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton113, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton112, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton111, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton110, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton121, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton120, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton119, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton118, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton117, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton116, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton130, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton132, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton90, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton89, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton91, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton86, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton87, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton88, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton122, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton96, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton95, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton97, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton92, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton93, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton94, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton124, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton98, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton99, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton100, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton102, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton101, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton103, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton126, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton109, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton108, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton107, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton106, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton105, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton104, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton128, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton115, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton114, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton113, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton112, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton111, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton110, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton130, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton121, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton120, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton119, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton118, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton117, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton116, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton132, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setForeground(new java.awt.Color(242, 242, 242));

        jPanel6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 20)); // NOI18N
        jLabel1.setText("ISTATISTIKLER");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel10.setText("jLabel10");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel11.setText("jLabel10");

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel12.setText("jLabel10");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel15.setText("jLabel10");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel16.setText("jLabel10");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(jLabel1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(28, 28, 28)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        jButton1.setText("Devam Et");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton123.setText("Test");
        jButton123.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton123ActionPerformed(evt);
            }
        });

        jButton125.setText("Yeni Oyun");
        jButton125.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton125ActionPerformed(evt);
            }
        });

        footer2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        footer2.setForeground(new java.awt.Color(51, 153, 255));
        footer2.setText("19011034-Eren Duman");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton123, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton125, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(footer2)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addComponent(footer2)
                        .addGap(76, 76, 76)
                        .addComponent(jButton123, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton125, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        sec13.setText("5");
        sec13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec13ActionPerformed(evt);
            }
        });

        sec10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sec10.setText("=");
        sec10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec10ActionPerformed(evt);
            }
        });

        sec14.setText("9");
        sec14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec14ActionPerformed(evt);
            }
        });

        sec11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sec11.setText("/");
        sec11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec11ActionPerformed(evt);
            }
        });

        sec15.setText("8");
        sec15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec15ActionPerformed(evt);
            }
        });

        sec12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sec12.setText("*");
        sec12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec12ActionPerformed(evt);
            }
        });

        sec16.setText("6");
        sec16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec16ActionPerformed(evt);
            }
        });

        jButton127.setText("Sonra Bitir");
        jButton127.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton127ActionPerformed(evt);
            }
        });

        sec17.setText("7");
        sec17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec17ActionPerformed(evt);
            }
        });

        jButton129.setText("Tahmin Et");
        jButton129.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton129ActionPerformed(evt);
            }
        });

        sec4.setText("3");
        sec4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec4ActionPerformed(evt);
            }
        });

        jButton131.setText("Sil");
        jButton131.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton131ActionPerformed(evt);
            }
        });

        sec5.setText("2");
        sec5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec5ActionPerformed(evt);
            }
        });

        sec6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sec6.setText("+");
        sec6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec6ActionPerformed(evt);
            }
        });

        sec7.setText("0");
        sec7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec7ActionPerformed(evt);
            }
        });

        sec8.setText("4");
        sec8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec8ActionPerformed(evt);
            }
        });

        sec9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        sec9.setText("-");
        sec9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec9ActionPerformed(evt);
            }
        });

        sec1.setText("1");
        sec1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sec1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(sec6, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec9, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec12, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec11, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec10, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(sec7, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec1, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec5, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec4, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec8, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(sec13, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec16, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec17, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec15, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sec14, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton127)
                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jButton131, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton129, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sec1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton129, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec7, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sec16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton131, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sec6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec12, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec10, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sec11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton127, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        seconds.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        seconds.setText("00");

        minutes.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        minutes.setText("00");

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N

        jButton136.setText("Yeniden Uret");
        jButton136.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton136ActionPerformed(evt);
            }
        });

        anaSayfa2.setText("Ana Sayfa");
        anaSayfa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anaSayfa2ActionPerformed(evt);
            }
        });

        footer1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        footer1.setForeground(new java.awt.Color(51, 153, 255));
        footer1.setText("19011034-Eren Duman");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(39, 39, 39)
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(95, 95, 95)
                                .addComponent(jButton136, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(anaSayfa2)))
                        .addGap(0, 49, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(footer1)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(jButton136, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addComponent(anaSayfa2)
                .addGap(18, 18, 18)
                .addComponent(footer1)
                .addContainerGap())
        );

        anaSayfa1.setText("Ana Sayfa");
        anaSayfa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                anaSayfa1ActionPerformed(evt);
            }
        });

        twoDot.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        twoDot.setText(":");

        footer.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        footer.setForeground(new java.awt.Color(51, 153, 255));
        footer.setText("19011034-Eren Duman");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(14, 14, 14)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(minutes, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(twoDot, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seconds, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(88, 88, 88)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(footer)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(anaSayfa1, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(seconds, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(minutes, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(twoDot))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(footer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(anaSayfa1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(51, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        choose = 1;
        panel = 3;
        line = 1;
        operation = "---";
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        choose = 6;
        panel = 3;
        line = 1; operation = "---";
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        choose = 5;
        panel = 3;
        line = 1; operation = "---";
    }//GEN-LAST:event_jButton2ActionPerformed

    private void sec1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec1ActionPerformed
        write(choose, line, panel, "1");
    }//GEN-LAST:event_sec1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        choose = 2;
        panel = 3;
        line = 1; operation = "---";        
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        choose = 3;
        panel = 3;
        line = 1;  operation = "---";
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        choose = 4;
        panel = 3;
        line = 1;   operation = "---";      

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        line = 2;
        panel = 3;
        choose = 4; operation = "---";
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        line = 2;
        panel = 3;
        choose = 5; operation = "---";
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        line = 2;
        panel = 3;
        choose = 6; operation = "---";
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        line = 2;
        panel = 3;
        choose = 2; operation = "---";
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        line = 2;
        panel = 3;
        choose = 1; operation = "---";
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        line = 2;
        panel = 3;
        choose = 3; operation = "---";
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        line = 3;
        panel = 3;
        choose = 6; operation = "---";
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        line = 3;
        panel = 3;
        choose = 5; operation = "---";
        
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        line = 3;
        panel = 3;
        choose = 4; operation = "---";
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        line = 3;
        panel = 3;
        choose = 3; operation = "---";
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        line = 3;
        panel = 3; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        line = 3;
        panel = 3;
        choose = 1; operation = "---";
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        line = 4;
        panel = 3;
        choose = 6; operation = "---";
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        line = 4;
        panel = 3;
        choose = 5; operation = "---";
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        line = 4;
        panel = 3; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        line = 4;
        panel = 3; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        line = 4;
        panel = 3; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        line = 4;
        panel = 3; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        line = 5;
        panel = 3; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        line = 5;
        panel = 3; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        line = 5;
        panel = 3; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        line = 5; operation = "---";
        panel = 3;
        choose = 3;
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        line = 5;
        panel = 3; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        line = 5;
        panel = 3; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        line = 6;
        panel = 3; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton32ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        line = 6;
        panel = 3; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        line = 6;
        panel = 3;
        choose = 4; operation = "---";
    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed
        line = 6;
        panel = 3;
        choose = 3; operation = "---";
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        line = 6;
        panel = 3; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton36ActionPerformed

    private void jButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton37ActionPerformed
        line = 6;
        panel = 3; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton37ActionPerformed

    private void jButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton38ActionPerformed
        line = 1;
        panel = 1; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton38ActionPerformed

    private void jButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton39ActionPerformed
        line = 1;
        panel = 1;
        choose = 5; operation = "---";
    }//GEN-LAST:event_jButton39ActionPerformed

    private void jButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton40ActionPerformed
        line = 1;
        panel = 1; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton40ActionPerformed

    private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
        line = 1;
        panel = 1;
        choose = 2; operation = "---";
    }//GEN-LAST:event_jButton41ActionPerformed

    private void jButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton42ActionPerformed
        line = 1;
        panel = 1; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton42ActionPerformed

    private void jButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton43ActionPerformed
        line = 1;
        panel = 1;
        choose = 3; operation = "---";
    }//GEN-LAST:event_jButton43ActionPerformed

    private void jButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton44ActionPerformed
        line = 2;
        panel = 1;
        choose = 4; operation = "---";
    }//GEN-LAST:event_jButton44ActionPerformed

    private void jButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton45ActionPerformed
        line = 2;
        panel = 1;
        choose = 5; operation = "---";
    }//GEN-LAST:event_jButton45ActionPerformed

    private void jButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton46ActionPerformed
        line = 2;
        panel = 1; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton46ActionPerformed

    private void jButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton47ActionPerformed
        line = 2;
        panel = 1; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton47ActionPerformed

    private void jButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton48ActionPerformed
        line = 2;
        panel = 1; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton48ActionPerformed

    private void jButton49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton49ActionPerformed
        line = 2;
        panel = 1;
        choose = 3; operation = "---";
    }//GEN-LAST:event_jButton49ActionPerformed

    private void jButton50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton50ActionPerformed
        line = 3;
        panel = 1;
        choose = 1; operation = "---";
    }//GEN-LAST:event_jButton50ActionPerformed

    private void jButton51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton51ActionPerformed
        line = 3;
        panel = 1; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton51ActionPerformed

    private void jButton52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton52ActionPerformed
        line = 3;
        panel = 1; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton52ActionPerformed

    private void jButton53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton53ActionPerformed
        line = 3;
        panel = 1; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton53ActionPerformed

    private void jButton54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton54ActionPerformed
        line = 3;
        panel = 1; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton54ActionPerformed

    private void jButton55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton55ActionPerformed
        line = 3; operation = "---";
        panel = 1;
        choose = 6;
    }//GEN-LAST:event_jButton55ActionPerformed

    private void jButton56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton56ActionPerformed
        line = 4;
        panel = 1;
        choose = 6; operation = "---";
    }//GEN-LAST:event_jButton56ActionPerformed

    private void jButton57ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton57ActionPerformed
        line = 4;
        panel = 1; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton57ActionPerformed

    private void jButton58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton58ActionPerformed
        line = 4;
        panel = 1;
        choose = 4; operation = "---";
    }//GEN-LAST:event_jButton58ActionPerformed

    private void jButton59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton59ActionPerformed
        line = 4;
        panel = 1; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton59ActionPerformed

    private void jButton60ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton60ActionPerformed
        line = 4;
        panel = 1;
        choose = 2; operation = "---";
    }//GEN-LAST:event_jButton60ActionPerformed

    private void jButton61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton61ActionPerformed
        line = 4;
        panel = 1;
        choose = 1; operation = "---";
    }//GEN-LAST:event_jButton61ActionPerformed

    private void jButton62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton62ActionPerformed
        line = 5;
        panel = 1; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton62ActionPerformed

    private void jButton63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton63ActionPerformed
        line = 5;
        panel = 1;
        choose = 5; operation = "---";
    }//GEN-LAST:event_jButton63ActionPerformed

    private void jButton64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton64ActionPerformed
        line = 5;
        panel = 1; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton64ActionPerformed

    private void jButton65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton65ActionPerformed
        line = 5;
        panel = 1;
        choose = 3; operation = "---";
    }//GEN-LAST:event_jButton65ActionPerformed

    private void jButton66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton66ActionPerformed
        line = 5;
        panel = 1;
        choose = 2; operation = "---";
    }//GEN-LAST:event_jButton66ActionPerformed

    private void jButton67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton67ActionPerformed
        line = 5;
        panel = 1; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton67ActionPerformed

    private void jButton68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton68ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton68ActionPerformed

    private void jButton69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton69ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton69ActionPerformed

    private void jButton70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton70ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton70ActionPerformed

    private void jButton71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton71ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton71ActionPerformed

    private void jButton72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton72ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton72ActionPerformed

    private void jButton73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton73ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton73ActionPerformed

    private void jButton74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton74ActionPerformed
        choose = 7;
        panel = 3; operation = "---";
        line = 1;
    }//GEN-LAST:event_jButton74ActionPerformed

    private void jButton75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton75ActionPerformed
        line = 1;
        panel = 3; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton75ActionPerformed

    private void jButton76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton76ActionPerformed
        line = 2;
        panel = 3;
        choose = 7; operation = "---";
    }//GEN-LAST:event_jButton76ActionPerformed

    private void jButton77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton77ActionPerformed
        line = 2;
        panel = 3; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton77ActionPerformed

    private void jButton78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton78ActionPerformed
        line = 3;
        panel = 3; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton78ActionPerformed

    private void jButton79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton79ActionPerformed
        line = 3;
        panel = 3;
        choose = 8; operation = "---";
    }//GEN-LAST:event_jButton79ActionPerformed

    private void jButton80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton80ActionPerformed
        line = 4;
        panel = 3; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton80ActionPerformed

    private void jButton81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton81ActionPerformed
        line = 4;
        panel = 3; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton81ActionPerformed

    private void jButton82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton82ActionPerformed
        line = 5;
        panel = 3;
        choose = 7; operation = "---";
    }//GEN-LAST:event_jButton82ActionPerformed

    private void jButton83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton83ActionPerformed
        line = 5;
        panel = 3; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton83ActionPerformed

    private void jButton84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton84ActionPerformed
        line = 6;
        panel = 3;
        choose = 7; operation = "---";
    }//GEN-LAST:event_jButton84ActionPerformed

    private void jButton85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton85ActionPerformed
        line = 6;
        panel = 3; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton85ActionPerformed

    private void jButton86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton86ActionPerformed
        line = 1;
        panel = 2;
        choose = 4; operation = "---";
    }//GEN-LAST:event_jButton86ActionPerformed

    private void jButton87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton87ActionPerformed
        line = 1;
        panel = 2;
        choose = 5; operation = "---";
    }//GEN-LAST:event_jButton87ActionPerformed

    private void jButton88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton88ActionPerformed
        line = 1;
        panel = 2; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton88ActionPerformed

    private void jButton89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton89ActionPerformed
        line = 1;
        panel = 2; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton89ActionPerformed

    private void jButton90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton90ActionPerformed
        line = 1;
        panel = 2; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton90ActionPerformed

    private void jButton91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton91ActionPerformed
        line = 1;
        panel = 2; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton91ActionPerformed

    private void jButton92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton92ActionPerformed
        line = 2;
        panel = 2; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton92ActionPerformed

    private void jButton93ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton93ActionPerformed
        line = 2;
        panel = 2; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton93ActionPerformed

    private void jButton94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton94ActionPerformed
        line = 2;
        panel = 2;
        choose = 6; operation = "---";
    }//GEN-LAST:event_jButton94ActionPerformed

    private void jButton95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton95ActionPerformed
        line = 2;
        panel = 2;
        choose = 2; operation = "---";
    }//GEN-LAST:event_jButton95ActionPerformed

    private void jButton96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton96ActionPerformed
        line = 2;
        panel = 2;
        choose = 1; operation = "---";
    }//GEN-LAST:event_jButton96ActionPerformed

    private void jButton97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton97ActionPerformed
        line = 2;
        panel = 2; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton97ActionPerformed

    private void jButton98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton98ActionPerformed
        line = 3;
        panel = 2;
        choose = 1; operation = "---";
    }//GEN-LAST:event_jButton98ActionPerformed

    private void jButton99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton99ActionPerformed
        line = 3;
        panel = 2;
        choose = 2; operation = "---";
    }//GEN-LAST:event_jButton99ActionPerformed

    private void jButton100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton100ActionPerformed
        line = 3;
        panel = 2; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton100ActionPerformed

    private void jButton101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton101ActionPerformed
        line = 3;
        panel = 2; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton101ActionPerformed

    private void jButton102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton102ActionPerformed
        line = 3;
        panel = 2; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton102ActionPerformed

    private void jButton103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton103ActionPerformed
        line = 3;
        panel = 2;
        choose = 6; operation = "---";
    }//GEN-LAST:event_jButton103ActionPerformed

    private void jButton104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton104ActionPerformed
        line = 4;
        panel = 2; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton104ActionPerformed

    private void jButton105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton105ActionPerformed
        line = 4;
        panel = 2; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton105ActionPerformed

    private void jButton106ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton106ActionPerformed
        line = 4;
        panel = 2; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton106ActionPerformed

    private void jButton107ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton107ActionPerformed
        line = 4;
        panel = 2; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton107ActionPerformed

    private void jButton108ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton108ActionPerformed
        line = 4;
        panel = 2; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton108ActionPerformed

    private void jButton109ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton109ActionPerformed
        line = 4;
        panel = 2; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton109ActionPerformed

    private void jButton110ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton110ActionPerformed
        line = 5;
        panel = 2; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton110ActionPerformed

    private void jButton111ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton111ActionPerformed
        line = 5;
        panel = 2; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton111ActionPerformed

    private void jButton112ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton112ActionPerformed
        line = 5;
        panel = 2; operation = "---";
        choose = 4;
    }//GEN-LAST:event_jButton112ActionPerformed

    private void jButton113ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton113ActionPerformed
        line = 5;
        panel = 2; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton113ActionPerformed

    private void jButton114ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton114ActionPerformed
        line = 5;
        panel = 2; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton114ActionPerformed

    private void jButton115ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton115ActionPerformed
        line = 5;
        panel = 2; operation = "---";
        choose = 1;
    }//GEN-LAST:event_jButton115ActionPerformed

    private void jButton116ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton116ActionPerformed
        line = 6;
        panel = 2; operation = "---";
        choose = 6;
    }//GEN-LAST:event_jButton116ActionPerformed

    private void jButton117ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton117ActionPerformed
        line = 6;
        panel = 2; operation = "---";
        choose = 5;
    }//GEN-LAST:event_jButton117ActionPerformed

    private void jButton118ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton118ActionPerformed
        line = 6;
        panel = 2;
        choose = 4; operation = "---";
    }//GEN-LAST:event_jButton118ActionPerformed

    private void jButton119ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton119ActionPerformed
        line = 6;
        panel = 2; operation = "---";
        choose = 3;
    }//GEN-LAST:event_jButton119ActionPerformed

    private void jButton120ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton120ActionPerformed
        line = 6;
        panel = 2; operation = "---";
        choose = 2;
    }//GEN-LAST:event_jButton120ActionPerformed

    private void jButton121ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton121ActionPerformed
        line = 6;
        panel = 2;
        choose = 1; operation = "---";
    }//GEN-LAST:event_jButton121ActionPerformed

    private void jButton122ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton122ActionPerformed
        line = 1;
        panel = 2; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton122ActionPerformed

    private void jButton124ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton124ActionPerformed
        line = 2;
        panel = 2; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton124ActionPerformed

    private void jButton126ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton126ActionPerformed
        line = 3;
        panel = 2; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton126ActionPerformed

    private void jButton128ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton128ActionPerformed
        line = 4;
        panel = 2; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton128ActionPerformed

    private void jButton130ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton130ActionPerformed
        line = 5;
        panel = 2; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton130ActionPerformed

    private void jButton132ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton132ActionPerformed
        line = 6;
        panel = 2;
        choose = 7; operation = "---";
    }//GEN-LAST:event_jButton132ActionPerformed

    private void sec4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec4ActionPerformed
        write(choose, line, panel, "3");
    }//GEN-LAST:event_sec4ActionPerformed

    private void sec5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec5ActionPerformed
        write(choose, line, panel, "2");
    }//GEN-LAST:event_sec5ActionPerformed

    private void sec6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec6ActionPerformed
        write(choose, line, panel, "+");
    }//GEN-LAST:event_sec6ActionPerformed

    private void sec7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec7ActionPerformed
        write(choose, line, panel, "0");
    }//GEN-LAST:event_sec7ActionPerformed

    private void sec8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec8ActionPerformed
        write(choose, line, panel, "4");
    }//GEN-LAST:event_sec8ActionPerformed

    private void sec9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec9ActionPerformed
        write(choose, line, panel, "-");
    }//GEN-LAST:event_sec9ActionPerformed

    private void sec10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec10ActionPerformed
       write(choose, line, panel, "=");
    }//GEN-LAST:event_sec10ActionPerformed

    private void sec11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec11ActionPerformed
        write(choose, line, panel, "/");
    }//GEN-LAST:event_sec11ActionPerformed

    private void sec12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec12ActionPerformed
        write(choose, line, panel, "*");
    }//GEN-LAST:event_sec12ActionPerformed

    private void sec13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec13ActionPerformed
        write(choose, line, panel, "5");
    }//GEN-LAST:event_sec13ActionPerformed

    private void sec14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec14ActionPerformed
        write(choose, line, panel, "9");
    }//GEN-LAST:event_sec14ActionPerformed

    private void sec15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec15ActionPerformed
        write(choose, line, panel, "8");
    }//GEN-LAST:event_sec15ActionPerformed

    private void sec16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec16ActionPerformed
        write(choose, line, panel, "6");
    }//GEN-LAST:event_sec16ActionPerformed

    private void sec17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sec17ActionPerformed
        write(choose, line, panel, "7");
    }//GEN-LAST:event_sec17ActionPerformed
//YENI OYUN
    private void jButton125ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton125ActionPerformed
        Color color = new Color(153,153,153);
        equation = generateEquation();
        enable = 1;
        footer.setVisible(true);
        int j, k;
        if(satirSayisi == 9){
            panel = 1;
        }
        if(satirSayisi == 7){
            panel = 2;
        }
        if(satirSayisi == 8){
            panel = 3;
            
        }
        //önceden kalma tablodakibilgiler sıfırlanır(renk ve sayılar)
        mainline = 1;
        for(j=0; j<9; j++){
            for(k=0; k<9; k++){
                paint(k+1, j+1, panel, color); 
                write(k+1, j+1, panel, "");
            }
            mainline++; // write ve paintteki if'lerin sağlanması için. sonda mainline 1'den başlatıldığı için bozulmaz
        }
        satirSayisi = equation.length();
        if(satirSayisi == 9){
            jPanel2.setVisible(true);
            jPanel7.setVisible(true);
            jPanel1.setVisible(false);
            jPanel3.setVisible(false);
            jPanel5.setVisible(false);
            seconds.setVisible(true);
            twoDot.setVisible(true);
            minutes.setVisible(true);
            chosenEquation = ",,,,,,,,,";
            panel = 1;
        }
        if(satirSayisi == 7){
            jPanel3.setVisible(true);
            jPanel7.setVisible(true);
            jPanel1.setVisible(false);
            jPanel2.setVisible(false);
            jPanel5.setVisible(false);
            seconds.setVisible(true);
            twoDot.setVisible(true);
            minutes.setVisible(true);
            chosenEquation = ",,,,,,,";
            panel = 2;
        }
        if(satirSayisi == 8){
            jPanel1.setVisible(true);
            jPanel7.setVisible(true);
            jPanel3.setVisible(false);
            jPanel2.setVisible(false);
            jPanel5.setVisible(false);
            seconds.setVisible(true);
            twoDot.setVisible(true);
            minutes.setVisible(true);
            chosenEquation = ",,,,,,,,";
            panel = 3;
        }
        int i;
        control = new int[satirSayisi];
        control2 = new int[satirSayisi];
        for(i=0; i<satirSayisi; i++){
            control[i] = 0;
            control2[i] = 0;
        }
        choose = 1;
        line = 1;
        mainline = 1;
        operation = "---";
        fLater="";
        jPanel7.setSize(250, 250);
        anaSayfa1.setVisible(true);
        
        minutes.setVisible(true);
        minutes.setText("0");
       seconds.setVisible(true);
       seconds.setText("0");
       timeControl = 0;
        start = 0;
    }//GEN-LAST:event_jButton125ActionPerformed
//SILME ISLEMI
    private void jButton131ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton131ActionPerformed
        write(choose, line, panel, "");
    }//GEN-LAST:event_jButton131ActionPerformed
//TAHMİN ET
    private void jButton129ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton129ActionPerformed
        int i, j;
        int isThere=0;
        int notTrue=0;
        
        if(enable == 0){
            return;
        }
        //Girilen denklem icinde , varsa(bosluklari virgul olarak tanımladım) tum satirin doldurulmasi gerektigine dair mesaj verilmeli.
        if(chosenEquation.contains(",")){
            jDialog1.setSize(200,200);
            jDialog1.setVisible(true);
            jDialog1.setLocation(800, 250);
        }
        //Girilen denklem dogru degilse mesaj verilir.
        else if(!isEquationTrue(chosenEquation)){
            jDialog4.setSize(230,230);
            jDialog4.setVisible(true);
            jDialog4.setLocation(800, 250);
        }
        else{
            for(i=0; i<satirSayisi; i++){
                isThere=0;
                if(chosenEquation.substring(i, i+1).compareTo(equation.substring(i, i+1)) == 0){
                    paint(i+1, mainline, panel, Color.green);
                    control[i] = 1; 
                    control2[i] = 1;
                }
                else{
                    for(j=0; j<satirSayisi; j++){
                        if(chosenEquation.substring(i, i+1).compareTo(equation.substring(j, j+1)) == 0){//Eger tam yerinde degil de baska bir yerde varsa sariya boyanmalı.
                            if(control[j] == 0 && isThere == 0){//O noktaya onceden gidilmemisse sariya boyanmali.
                            	if(!(chosenEquation.substring(j, j+1).equals(equation.substring(j, j+1)))) {
                            		paint(i+1, mainline, panel, Color.yellow);
                            		control[j] = 1;
                            		isThere = 1;
                            	}
                            }
                        }
                    }
                    if(isThere == 0){
                        paint(i+1, mainline, panel, Color.red);
                    }
                }
            }
            
            mainline++; 
            choose = 1;
            line++;
            operation = "---";
            
            for(i=0; i<satirSayisi; i++){
                control[i] = control2[i];
            }
            
            for(i=0; i<satirSayisi; i++){
                if(control[i] == 0){
                    notTrue = 1;
                }
            }
            if(notTrue == 0){
                enable = 0; // bir daha giris yapilamaması gerektiginden enable=0 olarak atanir.
                //eger devam et'ten gelinmis ve oyun bitirilmisse finishLater dosyasi sifirlanir.
                if(fromContinue == 1){
                    fLater = "";
                    try{
                        writeFinishLater();
                    }
                    catch(IOException ex){
                        Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                }
                
                jDialog3.setSize(250, 250);
                jDialog3.setVisible(true);
                jLabel14.setText("Harcanilan Sure: " + minutes.getText() + "dk. " + seconds.getText() + "sn.");
                jDialog3.setLocation(800, 250);
                try {
                    writeStatistics("win");
                    writeStatistics("averageLine");
                    writeStatistics("averageTime");
                    
                } catch (IOException e) {
                    Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
                }
                fLater = "";
                try{
                    writeUnfinished("");
                }
                catch(IOException ex){
                    Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, ex);
                }
                fromContinue = 0;
            }
            else if(mainline > 6){
                enable = 0;
                if(fromContinue == 1){
                    fLater = "";
                    try{
                        writeFinishLater();
                    }
                    catch(IOException ex){
                        Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, ex);
                    }  
                }
                jDialog2.setSize(200, 200);
                jLabel7.setText("Cevap" + equation);
                jDialog2.setVisible(true);
                jDialog2.setLocation(800, 250);
                try{
                    writeStatistics("lose");
                }
                catch(IOException ex){
                    Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, ex);
                }
                fLater = "";
                try{
                    writeUnfinished("");
                }
                catch(IOException ex){
                    Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, ex);
                }
                fromContinue = 0;
            }
            else{
                if(mainline==2){
                    fLater = equation + "\n" + chosenEquation + "\n";
                }
                else{
                    fLater = fLater + chosenEquation + "\n";
                }
                try{
                    writeUnfinished(chosenEquation);
                }
                catch(IOException ex){
                    Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(satirSayisi == 6){
                chosenEquation = ",,,,,,";
            }
            if(satirSayisi == 7){
                chosenEquation = ",,,,,,,";
            }
            if(satirSayisi == 8){
                chosenEquation = ",,,,,,,,";
            }
        }   
        
        
        
    }//GEN-LAST:event_jButton129ActionPerformed

    private void jButton123ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton123ActionPerformed
        String equ;   
        jPanel5.setVisible(false);
        jPanel4.setVisible(true);
        minutes.setVisible(false);
        seconds.setVisible(false);
        timeControl = 0;
        
        equ = generateEquation();
        jLabel13.setText(equ);
    }//GEN-LAST:event_jButton123ActionPerformed

    private void jButton127ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton127ActionPerformed
        
        
        try{
            writeFinishLater();
        }
        catch(IOException ex){
            Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        jPanel1.setVisible(false);
        if(fLater.equals("")){
            jButton1.setVisible(false);
        }
        else{
            jButton1.setVisible(true);
        }
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel5.setVisible(true);
        jPanel7.setVisible(false);
        minutes.setVisible(false);
        seconds.setVisible(false);
        twoDot.setVisible(false);
        anaSayfa1.setVisible(false);
        timeControl = 1;
        paintDefault();
        
        
    }//GEN-LAST:event_jButton127ActionPerformed
//DEVAM ET
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        File file = new File("finishLater.txt");
        int i;
        footer.setVisible(true);
        String tmp;
        if(!file.exists()){
            try {
                file.createNewFile();
            } 
            catch (IOException e) {
                Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
            }
            
        }
        try{
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            tmp = bReader.readLine(); // dakika okundu
            tmp = bReader.readLine(); // saniye okundu
            equation = bReader.readLine();
        }
        catch (IOException e){
            Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
        }
        satirSayisi = equation.length();
        
        if(satirSayisi == 9){
            jPanel2.setVisible(true);
            jPanel7.setVisible(true);
            jPanel1.setVisible(false);
            jPanel3.setVisible(false);
            jPanel5.setVisible(false);
            seconds.setVisible(true);
            twoDot.setVisible(true);
            minutes.setVisible(true);
            anaSayfa1.setVisible(true);
            panel = 1;
        }
        if(satirSayisi == 7){
            jPanel3.setVisible(true);
            jPanel7.setVisible(true);
            jPanel1.setVisible(false);
            jPanel2.setVisible(false);
            jPanel5.setVisible(false);
            seconds.setVisible(true);
            twoDot.setVisible(true);
            minutes.setVisible(true);
            anaSayfa1.setVisible(true);
            panel = 2;
        }
        if(satirSayisi == 8){
            jPanel1.setVisible(true);
            jPanel7.setVisible(true);
            jPanel3.setVisible(false);
            jPanel2.setVisible(false);
            jPanel5.setVisible(false);
            seconds.setVisible(true);
            twoDot.setVisible(true);
            minutes.setVisible(true);
            anaSayfa1.setVisible(true);
            panel = 3;
        }
        control = new int[satirSayisi];
        control2 = new int[satirSayisi];
        for(i=0; i<satirSayisi; i++){
            control[i] = 0;
            control2[i] = 0;
        }
        choose = 1;
        line = 1;
        mainline = 1;
        operation = "---";
        try{
            readFinishLater();
            initializeFLater();
        }
        catch(IOException e){
            Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
        }
        if(satirSayisi == 9){
            chosenEquation = ",,,,,,,,,";
        }
        if(satirSayisi == 7){
            chosenEquation = ",,,,,,,";
        }
        if(satirSayisi == 8){
            chosenEquation = ",,,,,,,,";
        }
        timeControl = -1;
        fromContinue = 1;
        start = 0;
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton134ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton134ActionPerformed
        jPanel5.setVisible(true);
        jPanel1.setVisible(false);
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel7.setVisible(false);
        jDialog3.setVisible(false);
        minutes.setVisible(false);
        seconds.setVisible(false);
        twoDot.setVisible(false);
        footer.setVisible(false);
        paintDefault();
        timeControl = 1;
        try{
            readStatistics();
        }
        catch(IOException e){
            Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
        }
        
        File file = new File("finishLater.txt");
        String tmp = null;
        
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            tmp = bReader.readLine();
        }
        catch(IOException e){
            
        }
        
       if(tmp == null){
           jButton1.setVisible(false);
       }
       else{
           jButton1.setVisible(true);
       }
    }//GEN-LAST:event_jButton134ActionPerformed

    private void jButton135ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton135ActionPerformed
        jPanel5.setVisible(true);
        jPanel1.setVisible(false);
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel7.setVisible(false);
        jDialog2.setVisible(false);
        minutes.setVisible(false);
        seconds.setVisible(false);
        twoDot.setVisible(false);
        footer.setVisible(false);
        anaSayfa1.setVisible(false);
        timeControl = 1;
        try{
            readStatistics();
        }
        catch(IOException e){
            Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
        }
        
        
        File file = new File("finishLater.txt");
        String tmp = null;
        
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            tmp = bReader.readLine();
        }
        catch(Exception e){
            
        }
        
       if(tmp == null){
           jButton1.setVisible(false);
       }
       else{
           jButton1.setVisible(true);
       }
    }//GEN-LAST:event_jButton135ActionPerformed

    private void jButton136ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton136ActionPerformed
        String equation;
        equation = generateEquation();
        jLabel13.setText(equation);
    }//GEN-LAST:event_jButton136ActionPerformed

    private void anaSayfa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anaSayfa2ActionPerformed
        anaSayfa1.setVisible(false);
        jPanel4.setVisible(false);
        twoDot.setVisible(false);
        minutes.setVisible(false);
        seconds.setVisible(false);
        jPanel5.setVisible(true);
    }//GEN-LAST:event_anaSayfa2ActionPerformed

    private void anaSayfa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anaSayfa1ActionPerformed
        File file = new File("finishLater.txt");
        String tmp = null;
        
        jPanel1.setVisible(false);
        jPanel2.setVisible(false);
        jPanel3.setVisible(false);
        jPanel5.setVisible(true);
        jPanel7.setVisible(false);
        minutes.setVisible(false);
        seconds.setVisible(false);
        twoDot.setVisible(false);
        anaSayfa1.setVisible(false);
        footer.setVisible(false);
        timeControl = 1;
        //Eger finishLater dosyası boşsa devam et butonu gözükmemeli
        try{
            if(!file.exists()){
                file.createNewFile();
            }
            FileReader fReader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(fReader);
            tmp = bReader.readLine();
        }
        catch(IOException e){
            
        }
        
        try{
            if(!isEmpty("unfinished.txt")){
                writeStatistics("unFinished");
                writeUnfinished("");
            }
        }
            catch(IOException e){
                Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
            }
            try{
                readStatistics();
            }
            catch(IOException e){
                Logger.getLogger(Nerdle.class.getName()).log(Level.SEVERE, null, e);
            }
        
       if(tmp == null){
           jButton1.setVisible(false);
       }
       else{
           jButton1.setVisible(true);
       }
       paintDefault();
    }//GEN-LAST:event_anaSayfa1ActionPerformed

    private void jButton137ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton137ActionPerformed
        line = 1;
        panel = 1; operation = "---";
        choose = 9;
    }//GEN-LAST:event_jButton137ActionPerformed

    private void jButton138ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton138ActionPerformed
        line = 2;
        panel = 1; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton138ActionPerformed

    private void jButton139ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton139ActionPerformed
        line = 1;
        panel = 1; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton139ActionPerformed

    private void jButton140ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton140ActionPerformed
        line = 3;
        panel = 1; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton140ActionPerformed

    private void jButton141ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton141ActionPerformed
        line = 3;
        panel = 1; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton141ActionPerformed

    private void jButton142ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton142ActionPerformed
        line = 4;
        panel = 1; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton142ActionPerformed

    private void jButton143ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton143ActionPerformed
        line = 5;
        panel = 1; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton143ActionPerformed

    private void jButton144ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton144ActionPerformed
        line = 5;
        panel = 1; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton144ActionPerformed

    private void jButton145ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton145ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 7;
    }//GEN-LAST:event_jButton145ActionPerformed

    private void jButton146ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton146ActionPerformed
        line = 2;
        panel = 1; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton146ActionPerformed

    private void jButton147ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton147ActionPerformed
        line = 4;
        panel = 1; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton147ActionPerformed

    private void jButton148ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton148ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton148ActionPerformed

    private void jButton149ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton149ActionPerformed
        line = 1;
        panel = 1; operation = "---";
        choose = 8;
    }//GEN-LAST:event_jButton149ActionPerformed

    private void jButton150ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton150ActionPerformed
        line = 2;
        panel = 1; operation = "---";
        choose = 9;
    }//GEN-LAST:event_jButton150ActionPerformed

    private void jButton151ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton151ActionPerformed
        line = 4;
        panel = 1; operation = "---";
        choose = 9;
    }//GEN-LAST:event_jButton151ActionPerformed

    private void jButton152ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton152ActionPerformed
        line = 3;
        panel = 1; operation = "---";
        choose = 9;
    }//GEN-LAST:event_jButton152ActionPerformed

    private void jButton153ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton153ActionPerformed
        line = 6;
        panel = 1; operation = "---";
        choose = 9;
    }//GEN-LAST:event_jButton153ActionPerformed

    private void jButton154ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton154ActionPerformed
        line = 5;
        panel = 1; operation = "---";
        choose = 9;
    }//GEN-LAST:event_jButton154ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Nerdle.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Nerdle().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton anaSayfa1;
    private javax.swing.JButton anaSayfa2;
    private javax.swing.JLabel footer;
    private javax.swing.JLabel footer1;
    private javax.swing.JLabel footer2;
    private javax.swing.JLabel footer3;
    private javax.swing.JLabel footer4;
    private javax.swing.JLabel footer5;
    private javax.swing.JLabel footer6;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton100;
    private javax.swing.JButton jButton101;
    private javax.swing.JButton jButton102;
    private javax.swing.JButton jButton103;
    private javax.swing.JButton jButton104;
    private javax.swing.JButton jButton105;
    private javax.swing.JButton jButton106;
    private javax.swing.JButton jButton107;
    private javax.swing.JButton jButton108;
    private javax.swing.JButton jButton109;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton110;
    private javax.swing.JButton jButton111;
    private javax.swing.JButton jButton112;
    private javax.swing.JButton jButton113;
    private javax.swing.JButton jButton114;
    private javax.swing.JButton jButton115;
    private javax.swing.JButton jButton116;
    private javax.swing.JButton jButton117;
    private javax.swing.JButton jButton118;
    private javax.swing.JButton jButton119;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton120;
    private javax.swing.JButton jButton121;
    private javax.swing.JButton jButton122;
    private javax.swing.JButton jButton123;
    private javax.swing.JButton jButton124;
    private javax.swing.JButton jButton125;
    private javax.swing.JButton jButton126;
    private javax.swing.JButton jButton127;
    private javax.swing.JButton jButton128;
    private javax.swing.JButton jButton129;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton130;
    private javax.swing.JButton jButton131;
    private javax.swing.JButton jButton132;
    private javax.swing.JButton jButton134;
    private javax.swing.JButton jButton135;
    private javax.swing.JButton jButton136;
    private javax.swing.JButton jButton137;
    private javax.swing.JButton jButton138;
    private javax.swing.JButton jButton139;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton140;
    private javax.swing.JButton jButton141;
    private javax.swing.JButton jButton142;
    private javax.swing.JButton jButton143;
    private javax.swing.JButton jButton144;
    private javax.swing.JButton jButton145;
    private javax.swing.JButton jButton146;
    private javax.swing.JButton jButton147;
    private javax.swing.JButton jButton148;
    private javax.swing.JButton jButton149;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton150;
    private javax.swing.JButton jButton151;
    private javax.swing.JButton jButton152;
    private javax.swing.JButton jButton153;
    private javax.swing.JButton jButton154;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton38;
    private javax.swing.JButton jButton39;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton40;
    private javax.swing.JButton jButton41;
    private javax.swing.JButton jButton42;
    private javax.swing.JButton jButton43;
    private javax.swing.JButton jButton44;
    private javax.swing.JButton jButton45;
    private javax.swing.JButton jButton46;
    private javax.swing.JButton jButton47;
    private javax.swing.JButton jButton48;
    private javax.swing.JButton jButton49;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton50;
    private javax.swing.JButton jButton51;
    private javax.swing.JButton jButton52;
    private javax.swing.JButton jButton53;
    private javax.swing.JButton jButton54;
    private javax.swing.JButton jButton55;
    private javax.swing.JButton jButton56;
    private javax.swing.JButton jButton57;
    private javax.swing.JButton jButton58;
    private javax.swing.JButton jButton59;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton60;
    private javax.swing.JButton jButton61;
    private javax.swing.JButton jButton62;
    private javax.swing.JButton jButton63;
    private javax.swing.JButton jButton64;
    private javax.swing.JButton jButton65;
    private javax.swing.JButton jButton66;
    private javax.swing.JButton jButton67;
    private javax.swing.JButton jButton68;
    private javax.swing.JButton jButton69;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton70;
    private javax.swing.JButton jButton71;
    private javax.swing.JButton jButton72;
    private javax.swing.JButton jButton73;
    private javax.swing.JButton jButton74;
    private javax.swing.JButton jButton75;
    private javax.swing.JButton jButton76;
    private javax.swing.JButton jButton77;
    private javax.swing.JButton jButton78;
    private javax.swing.JButton jButton79;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton80;
    private javax.swing.JButton jButton81;
    private javax.swing.JButton jButton82;
    private javax.swing.JButton jButton83;
    private javax.swing.JButton jButton84;
    private javax.swing.JButton jButton85;
    private javax.swing.JButton jButton86;
    private javax.swing.JButton jButton87;
    private javax.swing.JButton jButton88;
    private javax.swing.JButton jButton89;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButton90;
    private javax.swing.JButton jButton91;
    private javax.swing.JButton jButton92;
    private javax.swing.JButton jButton93;
    private javax.swing.JButton jButton94;
    private javax.swing.JButton jButton95;
    private javax.swing.JButton jButton96;
    private javax.swing.JButton jButton97;
    private javax.swing.JButton jButton98;
    private javax.swing.JButton jButton99;
    private javax.swing.JDialog jDialog1;
    private javax.swing.JDialog jDialog2;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JLabel minutes;
    private javax.swing.JButton sec1;
    private javax.swing.JButton sec10;
    private javax.swing.JButton sec11;
    private javax.swing.JButton sec12;
    private javax.swing.JButton sec13;
    private javax.swing.JButton sec14;
    private javax.swing.JButton sec15;
    private javax.swing.JButton sec16;
    private javax.swing.JButton sec17;
    private javax.swing.JButton sec4;
    private javax.swing.JButton sec5;
    private javax.swing.JButton sec6;
    private javax.swing.JButton sec7;
    private javax.swing.JButton sec8;
    private javax.swing.JButton sec9;
    private javax.swing.JLabel seconds;
    private javax.swing.JLabel twoDot;
    // End of variables declaration//GEN-END:variables
}
