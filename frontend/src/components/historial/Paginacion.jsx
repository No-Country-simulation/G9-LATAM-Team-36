export default function Paginacion({ page, totalPages, onPageChange }) {
    if (totalPages <= 1) return null;

    return (
        <div className="flex justify-between items-center mt-4 pt-4 border-t border-slate-800">
            <button
                className="btn btn-ghost"
                disabled={page === 0}
                onClick={() => onPageChange(page - 1)}
            >
                Anterior
            </button>
            <span className="text-sm text-slate-400">
        Página {page + 1} de {totalPages}
      </span>
            <button
                className="btn btn-ghost"
                disabled={page === totalPages - 1}
                onClick={() => onPageChange(page + 1)}
            >
                Siguiente
            </button>
        </div>
    );
}