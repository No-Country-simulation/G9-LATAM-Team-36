import { RadialBarChart, RadialBar, PolarAngleAxis } from "recharts";
import { estiloDe } from "../../lib/categoria";

export default function GaugeEficiencia({ categoria, probabilidad }) {
  const color = estiloDe(categoria).hex;
  const valor = Math.round(probabilidad * 100);
  const data = [{ name: categoria, value: valor, fill: color }];

  return (
    <div className="flex flex-col items-center">
      <div className="h-[80px] w-[200px] overflow-hidden">
        <div className="-mt-[100px]">
          <RadialBarChart
            width={200}
            height={200}
            cx="50%"
            cy="90%"
            innerRadius="70%"
            outerRadius="100%"
            barSize={16}
            startAngle={180}
            endAngle={0}
            data={data}
          >
            <PolarAngleAxis type="number" domain={[0, 100]} angleAxisId={0} tick={false} />
            <RadialBar dataKey="value" background clockWise cornerRadius={8} />
          </RadialBarChart>
        </div>
      </div>
      <p className="-mt-7 text-2xl font-bold" style={{ color }}>
        {valor}%
      </p>
    </div>
  );
}