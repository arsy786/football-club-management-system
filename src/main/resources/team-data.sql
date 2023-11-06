-- Insert teams only if they don't already exist (for postgres db)
INSERT INTO team (name, city, manager)
SELECT * FROM (VALUES
    ('Manchester United F.C.', 'Manchester', 'Erik ten Hag'),
    ('Manchester City F.C.', 'Manchester', 'Pep Guardiola'),
    ('Arsenal F.C.', 'London', 'Mikel Arteta'),
    ('Liverpool F.C.', 'Liverpool', 'Jurgen Klopp'),
    ('Chelsea F.C.', 'London', 'Thomas Tuchel'),
    ('Tottenham Hotspur F.C.', 'London', 'Antonio Conte'),
    ('Real Madrid CF', 'Madrid', 'Carlo Ancelotti'),
    ('FC Barcelona', 'Barcelona', 'Xavi Hernandez'),
    ('Atlético Madrid', 'Madrid', 'Diego Simeone'),
    ('FC Bayern Munich', 'Munich', 'Julian Nagelsmann'),
    ('Borussia Dortmund', 'Dortmund', 'Edin Terzic'),
    ('Paris Saint-Germain F.C.', 'Paris', 'Christophe Galtier'),
    ('Olympique de Marseille', 'Marseille', 'Igor Tudor'),
    ('Olympique Lyonnais', 'Lyon', 'Peter Bosz'),
    ('Atalanta B.C.', 'Bergamo', 'Gian Piero Gasperini'),
    ('A.S. Roma', 'Rome', 'José Mourinho'),
    ('A.C. Milan', 'Milan', 'Stefano Pioli'),
    ('Inter Milan', 'Milan', 'Simone Inzaghi'),
    ('S.S.C. Napoli', 'Naples', 'Luciano Spalletti'),
    ('Juventus F.C.', 'Turin', 'Massimiliano Allegri')
) AS team_data(name, city, manager)
WHERE NOT EXISTS (
    SELECT 1
    FROM team
    WHERE team.name = team_data.name
);