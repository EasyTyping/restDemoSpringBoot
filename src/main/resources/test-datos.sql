SELECT *
FROM articulos;
INSERT INTO articulos
(`idArticulo`,
 `nombreArticulo`,
 `descripcion`,
 `precioUnidad`,
 `stock`)
VALUES (HIBERNATE_SEQUENCE.NEXTVAL, 'Nike VARSITY COMPETE TR 3',
        'Super modernas te haran volar por el cielo y caminar por el agua',
        '254.8',
        '13');
