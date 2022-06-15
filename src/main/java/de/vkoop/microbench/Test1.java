package de.vkoop.microbench;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 2)
//@Timeout(time = 10, timeUnit = TimeUnit.SECONDS)
public class Test1{

    @Param({"100", "1000", "10000", "100000"})
    public int iterations;

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(Test1.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void test1(Blackhole bh){
        int sum = 0;
        for (int i = 0; i < iterations; i++) {
            sum += i;
        }
        bh.consume(sum);
    }

    @Benchmark
    public void test2(Blackhole bh){
        int result = IntStream.range(0, iterations)
                .parallel()
                .reduce(Integer::sum).getAsInt();
        bh.consume(result);
    }

    @Benchmark
    public void test3(Blackhole bh){
        int result = IntStream.range(0, iterations)
                .reduce(Integer::sum).getAsInt();
        bh.consume(result);
    }
}
