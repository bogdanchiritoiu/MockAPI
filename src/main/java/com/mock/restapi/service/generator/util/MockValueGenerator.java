package com.mock.restapi.service.generator.util;

import net.datafaker.Faker;

/**
 * Utility class providing methods to generate mock values for various data types.
 * The class leverages a third-party library to generate randomized mock data.
 * <br>
 * This class is intended to be used statically and cannot be instantiated.
 */
public final class MockValueGenerator
{
    private static final Faker faker = new Faker();

    // Private constructor to prevent instantiation
    private MockValueGenerator()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * Generates a random mock value for a specified data type.
     * This method supports generation of String, Integer, Double, and Boolean values.
     *
     * @param type      The Class object representing the type of value to generate
     * @param fieldName The name of the field for which the value is being generated
     *                  (currently unused, reserved for future implementation)
     * @return A randomly generated value of the specified type, or null if the type
     * is not supported
     */
    public static Object generateValueForType(Class<?> type, String fieldName)
    {
        if (String.class.equals(type))
        {
            return faker.lorem().word();
        }
        else if (Integer.class.equals(type) || int.class.equals(type))
        {
            return faker.number().numberBetween(1, 1000);
        }
        else if (Double.class.equals(type) || double.class.equals(type))
        {
            return faker.number().randomDouble(2, 0, 1000);
        }
        else if (Boolean.class.equals(type) || boolean.class.equals(type))
        {
            return faker.bool().bool();
        }

        return null;
    }

    //TODO to extend the functionality dynamic mapping based on fieldName and type
}
