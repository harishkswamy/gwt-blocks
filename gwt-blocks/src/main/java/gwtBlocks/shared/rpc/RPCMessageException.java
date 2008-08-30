package gwtBlocks.shared.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author hkrishna
 */
public class RPCMessageException extends RuntimeException implements IsSerializable
{
    private static final long serialVersionUID = 3776387283205215732L;

    public RPCMessageException()
    {

    }

    public RPCMessageException(String message)
    {
        super(message);
    }
}
