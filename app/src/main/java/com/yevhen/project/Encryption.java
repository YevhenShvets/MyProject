package com.yevhen.project;

public class Encryption {
    private static String _VocKod = "ABCDEFGHIJKLM01234NOPQRSTUVWXYZ56789"; // масив символів, якими заповнюється ключ
    private static int _VocKodSize = 36; // кількість символів у масиві
    private static int _KodSize = 5; // кількість цифр у ключі шифрування

    // Масив букв, які використовуються для шифруваня
    private static int _VocLettersKodSize = 26;
    private static String _VocLettersKod = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    // Масив цифр, які використовуються для шифруваня
    private static int _VocNumbersKodSize = 10;
    private static String _VocNumbersKod = "0123456789";
    //------------------------------------------------------------------
    // Масив всіх символів, які можуть бути у вхідному повідомленні
    private static int _VocCharSize=170;
    private static String _VocChar = "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz0123456789АаБбВвГгҐґДдЕеЄєЖжЗзИиІіЇїЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЬьЮюЯяЫыЁёЭэЪъ{}[]()<>\"\'\\|/*+-= _,.!?:;№#@$%^&`~";

    private static int Kod_int(char ch)
    {
        for(int i=0; i<_VocKodSize; i++)
            if(ch==_VocKod.charAt(i)) return i;

            return 0;
    }
    //-------------------------------------------------
//*** Шифрування повідомлення
    public static String Encrypt(String st, char[] Kod)
    {
        String answer=""; // для збереження результату функції
        char[] tmp_ch = new char[2]; // для тимчасового зберігання символів

        int i,j,k=0; // символ з повідомлення, символ з масиву символів, цифра з коду шифрування
        for(i=0; i<st.length(); i++) // проходимося по символам повідомлення
        {
            if(k>=_KodSize) k=0; // для циклічного використання коду шифрування

            for(j=0; j<_VocCharSize; j++) // проходимося по масиву всіх можливих символів
            {
                if(st.charAt(i)==_VocChar.charAt(j)) // якщо символ у повідомлені співпадає з символом у масиві
                {
                    // Якщо індекс символа + код >= за розмір всього шифрувального масиву
                    if((j+Kod_int(Kod[k]))>=_VocCharSize)
                    {
                        // Буква, яка шифрує символ (ділимо націло)
                        tmp_ch[0] = _VocLettersKod.charAt((int)((j+Kod_int(Kod[k])-_VocCharSize)/_VocNumbersKodSize));
                        // Цифра, яка шифрує символ (отримуємо остачу від ділення)
                        tmp_ch[1] = _VocNumbersKod.charAt((j+Kod_int(Kod[k])-_VocCharSize)%_VocNumbersKodSize);
                    }
                    else
                    {
                        // Буква, яка шифрує символ (ділимо націло)
                        tmp_ch[0] = _VocLettersKod.charAt((int)((j+Kod_int(Kod[k]))/_VocNumbersKodSize));
                        // Цифра, яка шифрує символ (отримуємо остачу від ділення)
                        tmp_ch[1] = _VocNumbersKod.charAt((j+Kod_int(Kod[k]))%_VocNumbersKodSize);
                    }
                    // Записуємо шифр даного символа
                    answer += tmp_ch[0]; answer += tmp_ch[1];
                    //--------------------------------------------------------
                    k++; // переходимо до наступного символу у коді шифрування
                    break;
                }
            }
        }
        return answer; // повертаємо зашифроване повідомлення
    }

    //*** Дешифрування повідомлення
    public static String Decrypt(String st, char[] Kod)
    {
        String answer=""; // для збереження результату функції
        boolean pair_flag=true; // відповідає за парність зчитуваного символа
        int move_size=0; // розмір зсуву

        int i,j,k=0; // символ з повідомлення, символ з масиву шифрування, цифра з коду шифрування
        for(i=0; i<st.length(); i++) // проходимося по символам повідомлення
        {
            if(k>=_KodSize) k=0; // для циклічного використання коду шифрування

            if(pair_flag)
            {
                pair_flag = false;
                //---------------------------------------------------------------
                for(j=0; j<_VocLettersKodSize; j++) // проходимося по масиву букв
                {
                    // Розшифровування букви з шифру
                    if(st.charAt(i)==_VocLettersKod.charAt(j)) {move_size = j*_VocNumbersKodSize; break;}
                }
            }
            else
            {
                pair_flag = true;
                //---------------------------------------------------------------
                for(j=0; j<_VocNumbersKodSize; j++) // проходимося по масиву цифр
                {
                    // Розшифровування цифри з шифру
                    if(st.charAt(i)==_VocNumbersKod.charAt(j)) {move_size += j; break;}
                }
                // Записуємо розшифровку даного символа
                if((move_size-Kod_int(Kod[k]))<0) // якщо зсув - код < 0
                    answer += _VocChar.charAt(_VocCharSize+(move_size-Kod_int(Kod[k])));
                else
                    answer += _VocChar.charAt(move_size-Kod_int(Kod[k]));
                //--------------------------------------------------------
                k++; // переходимо до наступного символу у коді шифрування
            }
        }
        return answer; // повертаємо зашифроване повідомлення
    }
}
