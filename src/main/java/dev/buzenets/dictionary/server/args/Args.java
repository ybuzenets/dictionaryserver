package dev.buzenets.dictionary.server.args;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import lombok.Getter;

@Parameters(separators = "=")
public class Args {
    @Parameter(names = {"--address", "--addr", "-a"}, description = "IP or Hostname to bind server")
    @Getter
    private String address = "127.0.0.1";

    @Parameter(names = {"--port", "-p"}, description = "Port number")
    @Getter
    private Integer port = 9000;
}
