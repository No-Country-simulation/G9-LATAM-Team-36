// Botón consistente. variant: "primary" (default) | "ghost".
export default function Button({ variant = "primary", className = "", ...props }) {
  const clase = variant === "ghost" ? "btn-ghost" : "btn-primary";
  return <button className={`${clase} ${className}`} {...props} />;
}
