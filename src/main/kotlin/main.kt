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
import java.util.*

fun main() {
    val bioma = Biome.builder().effects(
        BiomeEffects.builder()
            .waterColor(0xfca980)
            .skyColor(0xfca980)
            .fogColor(0xfca980)
            .grassColor(0xa65536)
            .biomeParticles(BiomeParticles(1F,BiomeParticles.DustParticle(1F,1F,3F,1F)))
            .build())
        .precipitation(Biome.Precipitation.SNOW)
        .name(NamespaceID.from("a:sexoo")).build()
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
        val player = event.player
        val A = ChangeGameStatePacket()
        A.reason = ChangeGameStatePacket.Reason.RAIN_LEVEL_CHANGE
        A.value = 1F//player.position.pitch/90
        val packetweather = ChangeGameStatePacket()
        player.sendMessage(""+packetweather.value)
        //player.playerConnection.sendPacket(A)
    }
    class Generatooooor : ChunkGenerator{
        override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
            for (x in 0 until Chunk.CHUNK_SIZE_X) for (z in 0 until Chunk.CHUNK_SIZE_Z) {
                for (y in 0..39) {
                    val bloquinzin: Block = Block.GRASS_BLOCK
                    batch.setBlock(x, y, z, bloquinzin)
                }
            }
        }

        override fun fillBiomes(biomes: Array<out Biome>, chunkX: Int, chunkZ: Int) {
            Arrays.fill(biomes, MinecraftServer.getBiomeManager().getByName(NamespaceID.from("a:sexoo")))
        }

        override fun getPopulators(): MutableList<ChunkPopulator>? {
            return null
        }

    }

    instanceContainer.chunkGenerator = Generatooooor()

    minecraftServer.start("localhost",25565)
    println("Hello Kotlin")
}
