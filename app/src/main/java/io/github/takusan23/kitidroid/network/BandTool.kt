package io.github.takusan23.kitidroid.network

object BandTool {

    /** EARFCNからバンドを割り出す */
    fun earfcnToBand(earfcn: Int): Int {
        /**
         * https://www.arib.or.jp/english/html/overview/doc/STD-T104v2_10/5_Appendix/Rel11/36/36101-b50.pdf
         *
         * 33ページ目、「Table 5.7.3-1: E-UTRA channel numbers」参照
         * */
        val bandList = listOf(
            BandData(1, 0, 599),
            BandData(2, 600, 1199),
            BandData(3, 1200, 1949),
            BandData(4, 1950, 2399),
            BandData(5, 2400, 2649),
            BandData(6, 2650, 2749),
            BandData(7, 2750, 3449),
            BandData(8, 3450, 3799),
            BandData(9, 3800, 4149),
            BandData(10, 4150, 4749),
            BandData(11, 4750, 4949),
            BandData(12, 5010, 5179),
            BandData(13, 5180, 5279),
            BandData(14, 5280, 5279),
            BandData(17, 5730, 5849),
            BandData(18, 5850, 5999),
            BandData(19, 6000, 6149),
            BandData(20, 6150, 6449),
            BandData(21, 6450, 6599),
            BandData(22, 6600, 7399),
            BandData(23, 7500, 7699),
            BandData(24, 7700, 8039),
            BandData(25, 8040, 8689),
            BandData(26, 8690, 9039),
            BandData(27, 9040, 9209),
            BandData(28, 9210, 9659),
            BandData(29, 9660, 9769),
            BandData(30, 9770, 9869),
            BandData(31, 9870, 9919),
            BandData(32, 9920, 10359),
            BandData(65, 65536, 66435),
            BandData(66, 66436, 67335),
            BandData(67, 67336, 67535),
            BandData(68, 67536, 67835),
            BandData(69, 67836, 68335),
            BandData(70, 68336, 68585),
            BandData(71, 68586, 68935),
        )
        // 探す
        return bandList.find { bandData -> earfcn in bandData.nDlMin..bandData.nDlMax }?.bandNumber ?: 0
    }

    /** NRARFCNからバンドに変換する */
    fun nrarfcnToBand(nrarfcn: Int): Int {
        /**
         * https://www.etsi.org/deliver/etsi_ts/138100_138199/13810101/15.08.02_60/ts_13810101v150802p.pdf
         *
         * 30ページ目、「Table 5.4.2.3-1: Applicable NR-ARFCN per operating band」参照
         * */
        val bandList = listOf(
            BandData(1, 422000, 434000),
            BandData(2, 386000, 398000),
            BandData(3, 361000, 376000),
            BandData(5, 173800, 178800),
            BandData(7, 524000, 538000),
            BandData(8, 185000, 192000),
            BandData(20, 158200, 164200),
            BandData(28, 151600, 160600),
            BandData(38, 514000, 524000),
            BandData(41, 499200, 537999),
            BandData(50, 286400, 303400),
            BandData(51, 285400, 286400),
            BandData(66, 422000, 440000),
            BandData(70, 399000, 404000),
            BandData(71, 123400, 130400),
            BandData(74, 295000, 303600),
            BandData(75, 286400, 303400),
            BandData(76, 285400, 286400),
            BandData(77, 620000, 680000),
            BandData(78, 620000, 653333),
            BandData(79, 693334, 733333),
            // 5G ミリ波
            BandData(257, 2054166, 2104165),
            BandData(258, 2016667, 2070832),
            BandData(260, 2229166, 2279165),
            BandData(261, 2070833, 2084999),
        )
        // 探す
        return bandList.find { bandData -> nrarfcn in bandData.nDlMin..bandData.nDlMax }?.bandNumber ?: 0
    }

    /**
     * バンドの表データ
     * @param bandNumber バンドの番号
     * @param nDlMin Earfcnここから
     * @param nDlMax Earfcnここまで
     * */
    data class BandData(val bandNumber: Int, val nDlMin: Int, val nDlMax: Int)

}