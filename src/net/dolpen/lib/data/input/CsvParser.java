package net.dolpen.lib.data.input;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * CSVのパースをするやつ
 */
public class CsvParser {

    private InputStream is;
    private Charset cs;

    /**
     * 文字列から読み込む
     *
     * @param s ソース文字列
     */
    public CsvParser(String s) {
        try {
            init(new ByteArrayInputStream(s.getBytes("UTF-8")), Charset.forName("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * ImputStreamから読み込む(MSのCSV向け)
     *
     * @param is ImputStream
     */
    public CsvParser(InputStream is) {
        init(is, Charset.forName("MS932"));
    }

    /**
     * ImputStreamから読み込む
     *
     * @param is ImputStream
     * @param s  文字エンコーディング
     */
    public CsvParser(InputStream is, String s) {
        init(is, Charset.forName(s));
    }

    /**
     * ImputStreamから読み込む
     *
     * @param is ImputStream
     * @param cs 文字エンコーディング
     */
    public CsvParser(InputStream is, Charset cs) {
        init(is, cs);
    }

    private void init(InputStream is, Charset cs) {
        this.is = is;
        this.cs = cs;
    }

    private static final char quote = '\"';


    /**
     * 文字列中のクオート文字を数える
     *
     * @param line 文字列
     * @return クオートの数
     */
    private int countQuote(String line) {
        int count = 0;
        for (char c : line.toCharArray()) {
            if (c == quote) count++;
        }
        return count;
    }

    /**
     * 文字列がレコード断片かどうかを判定する
     *
     * @param line 改行なし文字列
     * @return 判定
     */
    private boolean isFragmment(String line) {
        return countQuote(line) % 2 > 0;
    }

    /**
     * CSV的な行レコードを生成する
     *
     * @param reader reader
     * @return 0つ以上の改行を含む行レコード
     * @throws java.io.IOException
     */
    private String buildRecord(BufferedReader reader) throws IOException {
        String result = reader.readLine();
        if (result == null) return null;
        if (result.isEmpty()) return "";

        boolean inString = false;
        String line = result;
        String newline;
        StringBuilder buff = new StringBuilder();

        while (true) {
            inString = inString ^ isFragmment(line);
            if (inString) {
                buff.append(line);
                if ((newline = reader.readLine()) != null) {
                    buff.append('\n');
                    line = newline;
                    continue;
                }
                if (inString) {
                    buff.append(quote);
                }
                return buff.toString();
            } else {
                buff.append(line);
                return buff.toString();
            }
        }
    }

    /**
     * 行レコードを複数セルのデータに分割する
     *
     * @param src 行レコード
     * @return セル文字列データのリスト
     */
    private List<String> splitRecord(String src) {
        String[] columns = src.split(",");
        StringBuilder buff = new StringBuilder();
        List<String> dest = new ArrayList<String>();
        for (int index = 0; index < columns.length; index++) {
            String column = columns[index];
            int quotePos = column.indexOf(quote);
            if (quotePos < 0) {
                dest.add(column);
            } else {
                buff.append(column);
                boolean isInString = isFragmment(column);
                while (isInString) {
                    column = columns[++index];
                    buff.append(',').append(column);
                    isInString = isInString ^ isFragmment(column);
                }
                dest.add(unQuote(buff.toString()));
                buff.setLength(0);
            }
        }
        return dest;
    }

    /**
     * クオートを取る
     *
     * @param str 元テキスト
     * @return 変換後
     */
    private String unQuote(String str) {
        if (str.charAt(0) != quote || str.charAt(str.length() - 1) != quote) return str;
        if (str.length() == 2) return "";
        return new String(str.substring(1, str.length() - 2));
    }


    /**
     * 二次元リストを返す
     *
     * @return 二次元リスト
     */
    public List<List<String>> read() {
        List<List<String>> dest = new ArrayList<List<String>>();
        InputStreamReader reader = null;
        BufferedReader buff = null;
        try {
            reader = new InputStreamReader(is, cs);
            buff = new BufferedReader(reader);
            while (true) {
                String record = buildRecord(buff);
                if (record == null) break;
                if (record.isEmpty() || record.startsWith("#")) continue;
                dest.add(splitRecord(record));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (buff != null) {
                    buff.close();
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return dest;
    }
}