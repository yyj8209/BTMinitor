package com.wayful.DataProcessing;
// Copyright 2013 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
//  EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
//  LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

<<<<<<< HEAD
import biz.source_code.dsp.filter.IirFilter;
import biz.source_code.dsp.filter.IirFilterCoefficients;
=======
import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;
>>>>>>> dev

/**
 * Created by Administrator on 2020-2-28.
 */

public class DSP {
<<<<<<< HEAD
=======

>>>>>>> dev
    private static final double[] aL = {
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
            1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    private static final double[] bL = {
            -0.0000, 0.0011, 0.0013, 0.0020, 0.0030, 0.0041, 0.0055, 0.0072, 0.0091, 0.0113,
            0.0137, 0.0164, 0.0192, 0.0222, 0.0253, 0.0284, 0.0315, 0.0344, 0.0372, 0.0398,
            0.0420, 0.0439, 0.0453, 0.0463, 0.0468, 0.0468, 0.0463, 0.0453, 0.0439, 0.0420,
            0.0398, 0.0372, 0.0344, 0.0315 ,  0.0284, 0.0253, 0.0222, 0.0192, 0.0164, 0.0137,
<<<<<<< HEAD
            0.0113, 0.0091, 0.0072, 0.0055, 0.0041, 0.0030, 0.0020, 0.0013, 0.0011   -0.0000};
=======
            0.0113, 0.0091, 0.0072, 0.0055, 0.0041, 0.0030, 0.0020, 0.0013, 0.0011, -0.0000};
>>>>>>> dev
    private static final double[] b1 ={
            -5.96620306785644,
            55.5348905463184,
            -4.04827269864643,
            -3.71807811751640,
            -0.829153299945660,
            41.8628541031988,
            40.0559579305220,
            -15.4042446791761,
            -7.21067582560092,
            15.6372268578975};
    private static final double[] b2 ={
            -10.7538839348428, -4.45822367445657,
            -14.0838513835447, -4.40462955258791};
<<<<<<< HEAD
    private static final double[] w1 ={
            -1.0456, 78.1154, -1.0117  -17.2902,  0.8090, 71.4619, 23.6203  -28.7142, 14.9003  -23.5123,
            -1.4376  -62.0000,  2.2804,  9.3658, 25.3608  -35.6615, 12.9383,  9.4289  -17.0217, 27.7030,
            -1.1749, 43.8986,  0.8313, -0.8319, -2.7821, 10.2939,  9.2174,  3.5046,  4.0132,  7.1928,
            -9.3608, -4.3958, 17.2928, 36.7141  -32.9854,  1.7570,  0.0809, 22.1119, -8.1983,  2.3811};
    private static final double[] w2 ={
            -1.71191429012879, 30.1187564503126, 11.6237164112433, 0.418365505112282,
            -8.93981818987250, 19.0729459673551, -27.9957917338929, 9.74093404950915,
            8.91992626628695, -23.0791304745422, -2.53019376046130, 18.9351050064484,
            17.6795828327930, -10.0889596619955, -7.50884499165928,-37.3873784625298,
            -8.85898741029087, 12.8500399174412, 4.29620797648214, 5.00029216719227,
            1.62366119994519, -17.4431936583160, -13.7850396501138, 12.0674162390007,
            10.1359859680916,7.10101778045406, 21.6176281728838, -12.7757959619427,
            -14.8705814509213, 15.2343485670371,  -3.27973283132949, -3.23860761340763,
            -1.21892908427021, -2.28515208978814, -1.28696982825038, -1.42193162142784,
            0.0255738046998417, -1.69835092885469, -1.47417616000217, -4.30894726114663 };
=======
    private static final double[][] w1 ={
            {-1.0456, 78.1154, -1.0117, -17.2902,  0.8090, 71.4619, 23.6203, -28.7142, 14.9003, -23.5123},
            {-1.4376,  -62.0000,  2.2804,  9.3658, 25.3608,  -35.6615, 12.9383,  9.4289, -17.0217, 27.7030},
            {-1.1749, 43.8986,  0.8313, -0.8319, -2.7821, 10.2939,  9.2174,  3.5046,  4.0132,  7.1928},
            {-9.3608, -4.3958, 17.2928, 36.7141,  -32.9854,  1.7570,  0.0809, 22.1119, -8.1983,  2.3811}};
    private static final double[][] w2 ={
            {-1.71191429012879, 30.1187564503126, 11.6237164112433, 0.418365505112282},
            {-8.93981818987250, 19.0729459673551, -27.9957917338929, 9.74093404950915},
            {8.91992626628695, -23.0791304745422,-2.53019376046130, 18.9351050064484},
            {17.6795828327930, -10.0889596619955, -7.50884499165928, -37.3873784625298},
            {-8.85898741029087, 12.8500399174412, 4.29620797648214, 5.00029216719227},
            {1.62366119994519, -17.4431936583160, -13.7850396501138, 12.0674162390007},
            {10.1359859680916, 7.10101778045406, 21.6176281728838, -12.7757959619427},
            {-14.8705814509213, 15.2343485670371, -3.27973283132949, -3.23860761340763},
            {-1.21892908427021, -2.28515208978814, -1.28696982825038,-1.42193162142784},
            {0.0255738046998417, -1.69835092885469, -1.47417616000217, -4.30894726114663} };
>>>>>>> dev
    private static final double[] xmin ={
            2.29442905826068, 0.0309003784097399,
            1.37966965243459, 0.600272503428339};
    private static final double[] xmax ={
            3895.97201519769, 3748.55815521969,
            4138.55426306739, 5.61077854604016};
    private static final double ymin = -1;
    private static final double ymax = 1;

<<<<<<< HEAD
    public static double[] filterDat(double[] input, double[] bL, double[] aL) {
        double[]output = new double[input.length-1];
        int i;
        for(i=0;i<input.length;i++){
            output[i] = 0;
        }

        output = IIRFilter(input,aL,bL);
        return output;
    }

    public synchronized static  double[] IIRFilter(double[] signal, double[] a, double[] b) {

        double[] in = new double[b.length];
        double[] out = new double[a.length-1];

        double[] outData = new double[signal.length];

        for (int i = 0; i < signal.length; i++) {

            System.arraycopy(in, 0, in, 1, in.length - 1);
            in[0] = signal[i];

            //calculate y based on a and b coefficients
            //and in and out.
            float y = 0;
            for(int j = 0 ; j < b.length ; j++){
                y += b[j] * in[j];

            }

            for(int j = 0;j < a.length-1;j++){
                y -= a[j+1] * out[j];
            }

            //shift the out array
            System.arraycopy(out, 0, out, 1, out.length - 1);
            out[0] = y;

            outData[i] = y;


        }
        return outData;
=======

    /**
     　　* 滤波。
     　　* @param matrix 输入信号行向量。

     　　* @return 。
     　　*/
    public synchronized static  double[] FIRFilter(double[] column) {
        int lenB = bL.length;
        int lenD = column.length;
        double[] in = new double[lenB];
        double[] OutData = new double[lenD];

        int len = lenD + 2*lenB;
        double [] filterData = new double[len];
        for (int i = 0; i < lenD + 2*lenB; i++) {
            if(i<lenB)
                filterData[i] = column[0];
            else if(i<lenB+lenD)
                filterData[i] = column[ i - lenB];
            else
                filterData[i] = column[lenD - 1];
        }


        for (int i = 0; i < len; i++) {
            System.arraycopy(in, 0, in, 1, in.length - 1);
            in[0] = filterData[i];
            //calculate y based on a and b coefficients and in and out.
            double y = 0;
            for(int j = 0 ; j < bL.length ; j++){
                y += bL[j] * in[j];
            }
            if(i>lenB-1 && i<lenB+lenD)
            OutData[i-lenB] = y;
        }
        return OutData;
    }
    /**
     　　* 滤波。
     　　* @param matrix 输入信号矩阵，每行为一个信号。

     　　* @return 。
     　　*/
    public synchronized static  double[][] FIRFilter(double[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        double[][] OutData = new double[m][n];

        for (int k = 0; k < m; k++) {
            OutData[k] = FIRFilter( matrix[k] );
        }
        return OutData;
    }

    /**
     　　* 求峰值。
     　　* @param matrix 输入信号行向量、width 为目标信号宽度。

     　　* @return 1 个通道的峰值绝对值。
     　　*/
    public synchronized static  double PeakValue(double[] column,int width) {
        double Peak = 0D;
        double tmpV, tmpP;
        double Mean;
        boolean bPeakup = true;

        tmpV = column[0];   // 最大值
        tmpP = 0;

        // 第一步确定是峰值还是谷值
        for (int i = 0; i < column.length; i++) {
            tmpV = tmpV + column[i];
        }
        Mean = tmpV / (column.length);

        for (int i = 0; i < column.length; i++) {
            if (column[i] > Mean) tmpP++;
        }
        if (tmpP > column.length / 2) {
            bPeakup = false;
        }

        tmpV = column[0];
        // 第二步求出最大值/最小值及位置。
        for (int i = 1; i < column.length; i++) {
            if (bPeakup) {
                tmpV = tmpV < column[i] ? column[i] : tmpV;   // 找最大值
                Peak = tmpV;
                tmpP = i;
            } else {
                tmpV = tmpV > column[i] ? column[i] : tmpV;   // 找最小值
                Peak = tmpV;
                tmpP = i;
            }
        }

        tmpV = 0;
        int LS =  new Double( tmpP).intValue() - width - 40;
        int LE =  new Double( tmpP).intValue() - width ;
        int RS =  new Double( tmpP).intValue() + width ;
        int RE =  new Double( tmpP).intValue() + width + 40;
        if(LS<0) {
            for (int i = RS; i < RE; i++) {
                tmpV = tmpV + column[i];
            }
            Mean = tmpV / 40;
        }else if(RE > column.length){
            for (int i = LS; i < LE; i++) {
                tmpV = tmpV + column[i];
            }
            Mean = tmpV / 40;
        }else {
            for (int i = LS; i < LE; i++) {
                tmpV = tmpV + column[i];
            }
            for (int i = RS; i < RE; i++) {
                tmpV = tmpV + column[i];
            }
            Mean = tmpV / 80;
        }

        Peak = Math.abs( Peak - Mean );

        return Peak;

    }

    /**
     　　* 求峰值。
     　　* @param matrix 输入信号矩阵，每行为一个信号、width 为目标信号宽度。

     　　* @return 3 个通道的峰值绝对值。
     　　*/
    public synchronized static  double[] PeakValue(double[][] matrix,int width) {
        int m = matrix.length;
        int n = matrix[0].length;
        double[] Peak = new double[m];
        double tmpV, tmpP;
        double Mean;
        boolean bPeakup = true;

        for(int k = 0; k < m; k++) {
            tmpV = 0;              // 每行的最大值
            tmpP = 0D;              // 位置
            // 第一步确定是峰值还是谷值
            for (int i = 0; i < n; i ++ ) {
                tmpV = tmpV + matrix[k][i];
            }
            Mean = tmpV / (n);
            tmpV = 0D;
            for (int i = 0; i < n; i++) {
                if (matrix[k][i] > Mean) tmpP ++;
            }
            if (tmpP > n / 2) {
                bPeakup = false;
            }

            // 第二步求出最大值/最小值及位置。
            tmpV = matrix[k][0];
            for (int i = 1; i < n; i++) {
                if (bPeakup) {
                    tmpV = tmpV < matrix[k][i] ? matrix[k][i] : tmpV;   // 找最大值
                    Peak[k] = tmpV;
                    tmpP = i;
                } else {
                    tmpV = tmpV > matrix[k][i] ? matrix[k][i] : tmpV;   // 找最小值
                    Peak[k] = tmpV;
                    tmpP = i;
                }
            }

            tmpV = 0D;
            int LS = new Double(tmpP).intValue() - width - 40;
            int LE = new Double(tmpP).intValue() - width;
            int RS = new Double(tmpP).intValue() + width;
            int RE = new Double(tmpP).intValue() + width + 40;
            if (LS < 0) {
                for (int i = RS; i < RE; i++) {
                    tmpV = tmpV + matrix[k][i];
                }
                Mean = tmpV / 40;
            } else if(RE > n) {
                for (int i = LS; i < LE; i++) {
                    tmpV = tmpV + matrix[k][i];
                }
                Mean = tmpV / 40;
            }else{
                for (int i = LS; i < LE; i++) {
                    tmpV = tmpV + matrix[k][i];
                }
                for (int i = RS; i < RE; i++) {
                    tmpV = tmpV + matrix[k][i];
                }
                Mean = tmpV / 80;
            }
            Peak[k] = Math.abs( Peak[k] - Mean );
        }

        return Peak;

    }
    /**
     　　* V1/V3。
     　　* @param PeakValue 输入特征值、其余为归一化参数。

     　　* @return
     　　*/
    public synchronized static double V1toV3(double []PeakValue){
        if(PeakValue.length < 3)
            return PeakValue[0]/PeakValue[1];
        else
            return PeakValue[0]/PeakValue[2];
    }

    /**
     　　* 特征值向量经过神经网络前的归一化，见mapmaxmin。
     　　* @param Value输入特征值、其余为归一化参数。

     　　* @return 归一化数据
     　　*/
    public synchronized static double[] mapMaxMin( double []Value )
    {
        double [] Output = new double[Value.length];
        for(int i = 0; i < Value.length; i ++ )
            Output[i] = (Value[i] - ymin)*(ymax - ymin)/(xmax[i] - xmin[i]) + ymin;

        return Output;
    }

    /**
     　　* 特征值矩阵经过神经网络前的归一化，见mapmaxmin。
     　　* @param Value输入特征值、其余为归一化参数。

     　　* @return 归一化数据
     　　*/
    public synchronized static double[][] mapMaxMin( double [][]Value )
    {
        int m = Value[0].length;
        int n = Value.length;
        double [][] Output = new double[m][n];
        for (int k = 0; k < Value.length; k ++ ) {
            for (int i = 0; i < Value.length; i++)
                Output[k][i] = (Value[k][i] - ymin) * (ymax - ymin) / (xmax[k] - xmin[k]) + ymin;
        }
        return Output;
    }

    /**
     　　* 特征值经过神经网络，判别归类。
     　　* @param column输入特征值、其余为网络参数。

     　　* @return 一维向量，每一个数表示第几类
     　　*/
    public synchronized static int BPNN(  double [] column )
    {
        double tmp;
        Matrix matrixInput = DenseMatrix.Factory.importFromArray( column );    // n*4
        Matrix matrixW1 = DenseMatrix.Factory.importFromArray( w1 );  // 4*10
//        Matrix matrixB1 = DenseMatrix.Factory.importFromArray( b1 );  // 1*10
        Matrix matrixW2 = DenseMatrix.Factory.importFromArray( w2 );  // 10*4
//        Matrix matrixB2 = DenseMatrix.Factory.importFromArray( b2 );  // 1*4

        Matrix matrixTmp, matrixTmp2;
        matrixTmp = matrixInput.mtimes( matrixW1 );  // n*10
        for(int i = 0; i < matrixTmp.getRowCount(); i ++){
            for(int j = 0; j < matrixTmp.getColumnCount(); j ++){
                tmp = matrixTmp.getAsDouble( i,j );
                tmp =  tmp + b1[i];
                matrixTmp.setAsDouble(1/(1 + Math.exp(-tmp)), i, j );
            }
        }
        matrixTmp2 = matrixTmp.mtimes( matrixW2 );   // n*4
        int Row = new Double(matrixTmp2.getRowCount()).intValue();
        int Column = new Double(matrixTmp2.getColumnCount()).intValue();
        double[][] dTmp2 = matrixTmp2.toDoubleArray();

        // 求出每行的最大值所在的序号，即为分类。
        int Output = 0;
        double tmpV= dTmp2[0][0] ;
        for(int j = 0; j < Column; j ++){
            tmp = dTmp2[0][j] ;
            if(tmpV<tmp){
                tmpV = tmp;
                Output = j;
            }
        }

        return Output;

    }

    /**
    　　* 特征值经过神经网络，判别归类。
    　　* @param column输入特征值、其余为网络参数。

    　　* @return 一维向量，每一个数表示第几类
    　　*/
    public synchronized static int[] BPNN(  double [][] column )
    {
        int m = column[0].length;
        double tmp;
        Matrix matrixInput = DenseMatrix.Factory.importFromArray( column );    // n*4
        Matrix matrixW1 = DenseMatrix.Factory.importFromArray( w1 );  // 4*10
//        Matrix matrixB1 = DenseMatrix.Factory.importFromArray( b1 );  // 1*10
        Matrix matrixW2 = DenseMatrix.Factory.importFromArray( w2 );  // 10*4
//        Matrix matrixB2 = DenseMatrix.Factory.importFromArray( b2 );  // 1*4

        Matrix matrixTmp, matrixTmp2;
        matrixTmp = matrixInput.mtimes( matrixW1 );  // n*10
        for(int i = 0; i < matrixTmp.getRowCount(); i ++){
            for(int j = 0; j < matrixTmp.getColumnCount(); j ++){
                tmp = matrixTmp.getAsDouble( i,j );
                tmp =  tmp + b1[i];
                matrixTmp.setAsDouble(1/(1 + Math.exp(-tmp)), i, j );
            }
        }
        matrixTmp2 = matrixTmp.mtimes( matrixW2 );   // n*4
        for(int i = 0; i < matrixTmp2.getRowCount(); i ++){
            for(int j = 0; j < matrixTmp2.getColumnCount(); j ++){
                tmp = matrixTmp2.getAsDouble( i,j );
                matrixTmp2.setAsDouble(tmp + b2[i], i, j );
            }
        }

        // 求出每行的最大值所在的序号，即为分类。
        int n = new Double(matrixTmp2.getColumnCount()).intValue();
        int []Output = new int[m];
        double tmpV;

        for(int i = 0; i < m; i ++){
            Output[i] = 0;   //
            tmpV = matrixTmp2.getAsDouble( i,0 );
            for(int j = 0; j < n; j ++){
                tmp = matrixTmp2.getAsDouble( i,j );
                if(tmpV<tmp){
                    tmpV = tmp;
                    Output[i] = j;
                }
            }
        }

        return Output;

>>>>>>> dev
    }

}
