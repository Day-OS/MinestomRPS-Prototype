import de.articdive.jnoise.JNoise
import de.articdive.jnoise.interpolation.InterpolationType
import net.minestom.server.MinecraftServer
import net.minestom.server.coordinate.Pos
import net.minestom.server.entity.Entity
import net.minestom.server.entity.GameMode
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.event.player.PlayerTickEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.instance.InstanceManager
import net.minestom.server.network.packet.server.play.ChangeGameStatePacket
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.biomes.Biome
import net.minestom.server.world.biomes.BiomeEffects
import net.minestom.server.world.biomes.BiomeParticles


fun main() {
    /*
    val biomePerlinx = JNoise.newBuilder().perlin().setInterpolation(InterpolationType.LINEAR).setFrequency(0.8).setSeed(555).build()
    var printablestr = ""
    for(x in 1..10){
        for(y in 1..10){
            var offsetperlin = (biomePerlinx.getNoise(x.toDouble(),y.toDouble())*10).toInt()
            offsetperlin = Utils.mapNoise(offsetperlin, 1, 40)
            val stroffsetperlin = offsetperlin.toString()
            printablestr += "$stroffsetperlin | "
        }
        printablestr += "\n"
    }
    println(printablestr)
    */
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
        player.refreshFlying(true)
        event.setSpawningInstance(instanceContainer)
        player.respawnPoint = Pos(0.0, 42.0, 0.0)
    }
    globalEventHandler.addListener(
        PlayerTickEvent::class.java
    ) { event: PlayerTickEvent ->
        //event.player.entityMeta.pose = Entity.Pose.SLEEPING
        event.player.gameMode = GameMode.CREATIVE
        val a = ChangeGameStatePacket()
        a.reason = ChangeGameStatePacket.Reason.RAIN_LEVEL_CHANGE
        a.value = 1F
        //player.position.pitch/90
        //val packetweather = ChangeGameStatePacket()
        //player.sendMessage(""+packetweather.value)
        //player.playerConnection.sendPacket(A)
    }

    instanceContainer.chunkGenerator = WorldGenerator()

    minecraftServer.start("localhost",25565)
}
