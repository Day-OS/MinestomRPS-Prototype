import de.articdive.jnoise.JNoise
import de.articdive.jnoise.interpolation.InterpolationType
import net.minestom.server.MinecraftServer
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.ChunkGenerator
import net.minestom.server.instance.ChunkPopulator
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.biomes.Biome
import java.util.*

class WorldGenerator : ChunkGenerator {
    private val seed = 555
    //private val seed = Random()
    private val biomePerlin1 = JNoise.newBuilder().perlin().setInterpolation(InterpolationType.QUADRATIC).setFrequency(0.01).setSeed((seed).toLong()).build()
    private val biomePerlin2 = JNoise.newBuilder().perlin().setInterpolation(InterpolationType.QUADRATIC).setFrequency(0.02).setSeed((seed).toLong()).build()
    //private val biomePerlinH = JNoise.newBuilder().perlin().setInterpolation(InterpolationType.LINEAR).setFrequency(0.2).setSeed(-seed).build()
    override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
        for (x in 0 until Chunk.CHUNK_SIZE_X) for (z in 0 until Chunk.CHUNK_SIZE_Z) {
            fun placeBlockAtCurrentPlace(currentBlk: Block, y: Int){
                batch.setBlock(x, y, z, currentBlk)
            }
            for (y in 0..254) {
                val noiseOffset1 = biomePerlin1.getNoise(((chunkX)*16 + x).toDouble(), y.toDouble(),((chunkZ)*16 + z).toDouble())*100
                val height1 = Utils.map(noiseOffset1.toInt(), -100, 100, 1, 40)
                val noiseOffset2 = biomePerlin2.getNoise(((chunkX)*16 + x).toDouble(), y.toDouble(),((chunkZ)*16 + z).toDouble())*100
                val height2 = Utils.map(noiseOffset2.toInt(), -100, 100, 1, 80)
                //Must be setupped with "if" or switches because it puts one block at a time
                if (y < 1) placeBlockAtCurrentPlace(Block.BEDROCK, y)
                else {
                    //-----ISLAND GENERATION
                    if( y <= 40
                        && y < height1
                    ){
                        placeBlockAtCurrentPlace(Block.SAND, y)
                    }
                    else if(y <= 20) placeBlockAtCurrentPlace(Block.WATER, y)

                    //-----MOUNTAIN GENERATION
                    if( y <= 80
                        && height1 >= 20
                        && y < height2
                    ){
                        placeBlockAtCurrentPlace(Block.SAND, y)
                    }
                }
            }
        }
    }

    override fun fillBiomes(biomes: Array<out Biome>, chunkX: Int, chunkZ: Int) {
        Arrays.fill(biomes, MinecraftServer.getBiomeManager().getByName(NamespaceID.from("minecraft:plains")))
    }

    override fun getPopulators(): MutableList<ChunkPopulator>? {
        return null
    }
}