class Utils {
    companion object{
        fun map(number: Int, oldn1: Int,oldn2: Int,newn1: Int,newn2: Int): Int{
            val oldr = oldn2 - oldn1
            val newr = newn2 - newn1
            return (((number - oldn1) * newr) / oldr) + newn1
        }
        fun mapNoise(number: Int, newn1: Int,newn2: Int): Int{
            val n = if(number == 0) 1 else number
            val oldn1 = -50
            val oldn2 = 50
            val oldr = oldn2 - oldn1
            val newr = newn2 - newn1
            return (((n - oldn1) * newr) / oldr) + newn1
        }
    }
}