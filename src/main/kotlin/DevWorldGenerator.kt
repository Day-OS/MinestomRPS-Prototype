import net.minestom.server.MinecraftServer
import net.minestom.server.instance.Chunk
import net.minestom.server.instance.ChunkGenerator
import net.minestom.server.instance.ChunkPopulator
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.instance.block.Block
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.biomes.Biome
import java.util.*

class DevWorldGenerator : ChunkGenerator {
    override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
        for (x in 0 until Chunk.CHUNK_SIZE_X) for (z in 0 until Chunk.CHUNK_SIZE_Z) {
            fun placeBlockAtCurrentPlace(currentBlk: Block, y: Int){
                batch.setBlock(x, y, z, currentBlk)
            }
            placeBlockAtCurrentPlace(Block.BEDROCK, 40)
        }
    }

    override fun fillBiomes(biomes: Array<out Biome>, chunkX: Int, chunkZ: Int) {
        Arrays.fill(biomes, MinecraftServer.getBiomeManager().getByName(NamespaceID.from("minecraft:plains")))
    }

    override fun getPopulators(): MutableList<ChunkPopulator>? {
        return null
    }
}