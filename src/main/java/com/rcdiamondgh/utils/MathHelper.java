package com.rcdiamondgh.utils;

import java.util.*;

public class MathHelper {

    public static double radianToDegree(final double angel) {
        final double b = 0.017453292519943295;
        return angel * b;
    }

    public static double degreeToRadian(final double radian) {
        final double b = 0.017453292519943295;
        return radian / b;
    }

    public static double getPolygonDegree(final double sideNum) {
        return 180.0 * (sideNum - 2.0) / sideNum;
    }

    public static double getPolygonSum(final double sideNum) {
        return (sideNum - 2.0) * 180.0;
    }

    public static double getSquare(final double num, final int a) {
        double b = 1.0;
        for (int x = 0; x < a; ++x) {
            b *= num;
        }
        return b;
    }

    public static double getSquare(final double num, final double th) {
        final int[] a = toFraction(th);
        double foot = getSquare(num, a[0]);
        foot = getRoot(foot, a[1]);
        return foot;
    }

    public static double getRoot(final double num, final int r) {
        double a = 0.0;
        double b = num;
        double c = 0.0;
        for (int aa = 0; aa < 60; ++aa) {
            c = (a + b) / 2.0;
            if (getSquare(c, r) > num) {
                b = c;
            } else if (getSquare(c, r) < num) {
                a = c;
            }
        }
        return c;
    }

    public static int getPointLength(final double a) {
        int foot = 0;
        int x = 0;
        final double c = a;
        final String str = "" + c;
        final char[] cr = str.toCharArray();
        for (int t = 0; t < cr.length; ++t) {
            if (cr[t] == '.') {
                x = t;
                break;
            }
        }
        foot = cr.length - x - 1;
        return foot;
    }

    public static int[] getDivisibles(final int num) {
        final int a = num;
        final int[] nums = new int[a];
        for (int x = 0; x < a; ++x) {
            nums[x] = x + 1;
        }
        final List<Integer> needs = new ArrayList<Integer>();
        for (final int x2 : nums) {
            if (num % x2 == 0) {
                needs.add(x2);
            }
        }
        needs.add(1);
        needs.add(num);
        final int[] ss = new int[needs.size()];
        for (int p = 0; p < ss.length; ++p) {
            ss[p] = needs.get(p);
        }
        return ss;
    }

    public static int getMostCnum(final int n1, final int n2) {
        final int[] a = getDivisibles(n1);
        final int[] b = getDivisibles(n2);
        int foot = 0;
        final List<Integer> ans = new ArrayList<Integer>();
        for (int x = 0; x < a.length; ++x) {
            final int us = a[x];
            for (final int e : b) {
                if (e == us) {
                    ans.add(e);
                }
            }
        }
        for (final int f : ans) {
            if (foot <= f) {
                foot = f;
            }
        }
        return foot;
    }

    public static int[] toFraction(double num) {
        final int a = getPointLength(num);
        int b = 1;
        final int[] foot = { 0, 0 };
        for (int x = 0; x < a; ++x) {
            num *= 10.0;
            b *= 10;
        }
        foot[0] = (int) num;
        foot[1] = b;
        final int c = getMostCnum(foot[0], foot[1]);
        foot[0] /= c;
        foot[1] /= c;
        return foot;
    }
}
