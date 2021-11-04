import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerTickEvent
import net.minestom.server.instance.*
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.instance.block.Block
import net.minestom.server.network.packet.server.play.ChangeGameStatePacket
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.biomes.Biome
import net.minestom.server.world.biomes.BiomeEffects
import net.minestom.server.world.biomes.BiomeParticles
import de.articdive.jnoise.JNoise
import de.articdive.jnoise.interpolation.InterpolationType
import java.util.*
import kotlin.math.abs


fun main() {
    val biomePerlinx = JNoise.newBuilder().perlin().setInterpolation(InterpolationType.LINEAR).setFrequency(0.2).setSeed(555).build()
    var printablestr = ""
    for(x in 1..10){
        for(y in 1..10){
            val offsetperlin = (biomePerlinx.getNoise(x.toDouble(),y.toDouble())*10 ).toInt()
            var stroffsetperlin = offsetperlin.toString()
            if (offsetperlin >= 0){
                stroffsetperlin = "+$offsetperlin"
            }
            printablestr += "$stroffsetperlin | "
        }
        printablestr += "\n"
    }
    println(printablestr)
    val bioma = Biome.builder().effects(
        BiomeEffects.builder()
            .waterColor(0xfca980)
            .skyColor(0xfca980)
            .fogColor(0xfca980)
            .grassColor(0xa65536)
            .biomeParticles(BiomeParticles(1F,BiomeParticles.DustParticle(1F,1F,3F,1F)))
            .build())
        .precipitation(Biome.Precipitation.SNOW)
        .name(NamespaceID.from("a:cbiome")).build()
    val minecraftServer: MinecraftServer = MinecraftServer.init()
    val instanceManager: InstanceManager = MinecraftServer.getInstanceManager()
    val instanceContainer: InstanceContainer = instanceManager.createInstanceContainer()
    val globalEventHandler = MinecraftServer.getGlobalEventHandler()
    MinecraftServer.getBiomeManager().addBiome(bioma)


    globalEventHandler.addListener(
        PlayerLoginEvent::class.java
    ) { event: PlayerLoginEvent ->
        event.player.gameMode = GameMode.CREATIVE
        val player = event.player
        player.isAllowFlying = true
        event.setSpawningInstance(instanceContainer)
        player.respawnPoint = Pos(0.0, 42.0, 0.0)
    }
    globalEventHandler.addListener(
        PlayerTickEvent::class.java
    ) { event: PlayerTickEvent ->
        event.player.gameMode = GameMode.CREATIVE
        //val player = event.player
        val a = ChangeGameStatePacket()
        a.reason = ChangeGameStatePacket.Reason.RAIN_LEVEL_CHANGE
        a.value = 1F//player.position.pitch/90
        //val packetweather = ChangeGameStatePacket()
        //player.sendMessage(""+packetweather.value)
        //player.playerConnection.sendPacket(A)
    }
    class Generatooooor : ChunkGenerator{
        private val seed = 555
        //private val seed = Random()
        private val biomePerlinV = JNoise.newBuilder().perlin().setInterpolation(InterpolationType.LINEAR).setFrequency(0.2).setSeed((seed).toLong()).build()
        //private val biomePerlinH = JNoise.newBuilder().perlin().setInterpolation(InterpolationType.LINEAR).setFrequency(0.2).setSeed(-seed).build()
        override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
            for (x in 0 until Chunk.CHUNK_SIZE_X) for (z in 0 until Chunk.CHUNK_SIZE_Z) {
                val noiseOffset = biomePerlinV.getNoise(((chunkX)*16 + x).toDouble(),((chunkZ)*16 + z).toDouble())*10
                fun getNoiseOffset(frequency: Int): Int{return (noiseOffset/frequency).toInt()}
                fun placeBlockAtCurrentPlace(currentBlk: Block, y: Int){
                    batch.setBlock(x, y, z, currentBlk)
                }
                for (y in 0..254) {
                    //Must be setupped with "if" or switches because it puts one block at a time
                    if (y < 1) placeBlockAtCurrentPlace(Block.BEDROCK, y)
                    else if(y < 10){
                        if(/*getNoiseOffset(1) < 0 ||*/ abs(getNoiseOffset(1) - y) < 4){
                            placeBlockAtCurrentPlace(Block.SAND, y)
                        }
                        else{
                            placeBlockAtCurrentPlace(Block.WATER, y)}
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

    instanceContainer.chunkGenerator = Generatooooor()

    minecraftServer.start("localhost",25565)
}
