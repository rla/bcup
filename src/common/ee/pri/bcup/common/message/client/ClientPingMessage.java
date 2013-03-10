package ee.pri.bcup.common.message.client;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ClientPingMessage extends ClientMessage {}
