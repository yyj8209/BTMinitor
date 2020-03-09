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

import org.ujmp.core.DenseMatrix;
import org.ujmp.core.Matrix;

/**
 * Created by Administrator on 2020-2-28.
 */

public class DSP {

    private static final double[] bL = {
            0.0032, 0.0085, 0.0060, 0.0107, 0.0127, 0.0164, 0.0198, 0.0237, 0.0276,
            0.0316, 0.0355, 0.0393, 0.0428, 0.0458, 0.0484, 0.0505, 0.0518, 0.0525,
            0.0525, 0.0518, 0.0505, 0.0484, 0.0458, 0.0428, 0.0393, 0.0355, 0.0316,
            0.0276, 0.0237, 0.0198, 0.0164, 0.0127, 0.0107, 0.0060, 0.0085, 0.0032};
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
    private static final double[][] w1 ={
            {-1.0456, 78.1154, -1.0117, -17.2902,  0.8090, 71.4619, 23.6203, -28.7142, 14.9003, -23.5123},
            {-1.4376,  -62.0000,  2.2804,  9.3658, 25.3608,  -35.6615, 12.9383,  9.4289, -17.0217, 27.7030},
            {-1.1749, 43.8986,  0.8313, -0.8319, -2.7821, 10.2939,  9.2174,  3.5046,  4.0132,  7.1928},
            {-9.3608, -4.3958, 17.2928, 36.7141,  -32.9854,  1.7570,  0.0809, 22.1119, -8.1983,  2.3811}};

    private static final double[][] w2 ={
            {-1.7119, -2.5302, 1.6237, -3.2797},
            {30.1188, 18.9351, -17.4432, -3.2386},
            {11.6237, 17.6796, -13.7850, -1.2189},
            {0.4184, -10.0890, 12.0674, -2.2852},
            {-8.9398, -7.5088, 10.1360, -1.2870},
            {19.0729, -37.3874, 7.1010, -1.4219},
            {-27.9958, -8.8590, 21.6176, 0.0256},
            {9.7409, 12.8500, -12.7758, -1.6984},
            {8.9199, 4.2962, -14.8706, -1.4742},
            {-23.0791, 5.0003, 15.2343, -4.3089} };

    private static final double[] xmin ={
            2.29442905826068, 0.0309003784097399,
            1.37966965243459, 0.600272503428339};
    private static final double[] xmax ={
            3895.97201519769, 3748.55815521969,
            4138.55426306739, 5.61077854604016};
    private static final double ymin = -1;
    private static final double ymax = 1;


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
        for (int i = 1; i < column.length; i++) {
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
                if(tmpV < column[i]) {
                    tmpV = column[i];    // 找最大值
                    Peak = tmpV;
                    tmpP = i;
                }
            } else {
                if(tmpV > column[i]) {
                    tmpV = column[i];    // 找最小值
                    Peak = tmpV;
                    tmpP = i;
                }
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
        double[] Peak = new double[m];
        for(int i = 0;i < m; i ++){
            Peak[i] = PeakValue(matrix[i],width);
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
            Output[i] = (Value[i] - xmin[i])*(ymax - ymin)/(xmax[i] - xmin[i]) + ymin;

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
                tmp =  tmp + b1[j];
                matrixTmp.setAsDouble(1/(1 + Math.exp(-tmp)), i, j );
            }
        }
        matrixTmp2 = matrixTmp.mtimes( matrixW2 );   // n*4
        int Column = new Double(matrixTmp2.getColumnCount()).intValue();
        double[][] dTmp2 = matrixTmp2.toDoubleArray();
        for(int i = 0; i < matrixTmp2.getRowCount(); i ++){
            for(int j = 0; j < matrixTmp2.getColumnCount(); j ++){
                tmp = matrixTmp2.getAsDouble( i,j );
                matrixTmp2.setAsDouble(tmp + b2[j], i, j );
            }
        }

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

    }

}
