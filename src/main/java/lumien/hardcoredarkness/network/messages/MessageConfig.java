package lumien.hardcoredarkness.network.messages;

import java.util.HashSet;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import lumien.hardcoredarkness.HardcoreDarkness;
import lumien.hardcoredarkness.config.HardcoreDarknessConfig;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageConfig implements IMessage,IMessageHandler<MessageConfig,IMessage>
{
	HardcoreDarknessConfig config;
	
	public MessageConfig()
	{
		
	}
	
	public MessageConfig(HardcoreDarknessConfig config)
	{
		this.config = config;
	}
	
	@Override
	public IMessage onMessage(MessageConfig message, MessageContext ctx)
	{
		HardcoreDarknessConfig config = message.config;
		
		HardcoreDarkness.INSTANCE.logger.log(Level.DEBUG, "Received Hardcore Darkness Config from Server: "+config.toString());
		
		HardcoreDarkness.INSTANCE.setServerConfig(config);
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.config = new HardcoreDarknessConfig();
		
		config.setMode(buf.readInt());
		config.setDarkNether(buf.readBoolean());
		config.setDarkEnd(buf.readBoolean());
		config.setAlternativeNightSkylight(buf.readBoolean());
		
		int blackListSize = buf.readInt();

		for (int i = 0; i < blackListSize; i++)
		{
			config.addDimensionToBlacklist(buf.readInt());
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(config.getMode());
		buf.writeBoolean(config.darkNether());
		buf.writeBoolean(config.darkEnd());
		buf.writeBoolean(config.removeBlue());
		
		HashSet<Integer> blackList = config.getDimensionBlackList();
		buf.writeInt(blackList.size());
		for (Integer dimension : blackList)
		{
			buf.writeInt(dimension);
		}
	}
}
